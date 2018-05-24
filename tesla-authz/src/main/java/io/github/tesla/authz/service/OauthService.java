
package io.github.tesla.authz.service;

import java.util.Set;

import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.tesla.authz.DefaultAuthenticationIdGenerator;
import io.github.tesla.authz.dao.Oauth2Dao;
import io.github.tesla.authz.domain.AccessToken;
import io.github.tesla.authz.domain.ClientDetails;
import io.github.tesla.authz.domain.OauthCode;

@Service
public class OauthService {

  private static final Logger LOG = LoggerFactory.getLogger(OauthService.class);

  @Autowired
  private Oauth2Dao oauthRepository;

  @Autowired
  private DefaultAuthenticationIdGenerator authenticationIdGenerator;

  @Autowired
  private OAuthIssuer oAuthIssuer;


  public ClientDetails loadClientDetails(String clientId) {
    LOG.debug("Load ClientDetails by clientId: {}", clientId);
    return oauthRepository.findClientDetails(clientId);
  }

  public OauthCode saveAuthorizationCode(String authCode, ClientDetails clientDetails) {
    final String username = currentUsername();
    OauthCode oauthCode =
        new OauthCode().code(authCode).username(username).clientId(clientDetails.getClientId());

    oauthRepository.saveOauthCode(oauthCode);
    LOG.debug("Save OauthCode: {}", oauthCode);
    return oauthCode;
  }

  private String currentUsername() {
    return SecurityUtils.getSubject().getPrincipals().getRealmNames().iterator().next();
  }


  public String retrieveAuthCode(ClientDetails clientDetails) throws OAuthSystemException {
    final String clientId = clientDetails.getClientId();
    final String username = currentUsername();

    OauthCode oauthCode = oauthRepository.findOauthCodeByUsernameClientId(username, clientId);
    if (oauthCode != null) {
      LOG.debug("OauthCode ({}) is existed, remove it and create a new one", oauthCode);
      oauthRepository.deleteOauthCode(oauthCode);
    }
    oauthCode = createOauthCode(clientDetails);
    return oauthCode.code();
  }



  public AccessToken retrieveAccessToken(ClientDetails clientDetails, Set<String> scopes,
      boolean includeRefreshToken) throws OAuthSystemException {
    String scope = OAuthUtils.encodeScopes(scopes);
    final String username = currentUsername();
    final String clientId = clientDetails.getClientId();
    final String authenticationId = authenticationIdGenerator.generate(clientId, username, scope);
    AccessToken accessToken = oauthRepository.findAccessToken(clientId, username, authenticationId);
    if (accessToken == null) {
      accessToken =
          createAndSaveAccessToken(clientDetails, includeRefreshToken, username, authenticationId);
      LOG.debug("Create a new AccessToken: {}", accessToken);
    }
    return accessToken;
  }

  public AccessToken retrieveNewAccessToken(ClientDetails clientDetails, Set<String> scopes)
      throws OAuthSystemException {
    String scope = OAuthUtils.encodeScopes(scopes);
    final String username = currentUsername();
    final String clientId = clientDetails.getClientId();
    final String authenticationId = authenticationIdGenerator.generate(clientId, username, scope);
    AccessToken accessToken = oauthRepository.findAccessToken(clientId, username, authenticationId);
    if (accessToken != null) {
      LOG.debug("Delete existed AccessToken: {}", accessToken);
      oauthRepository.deleteAccessToken(accessToken);
    }
    accessToken = createAndSaveAccessToken(clientDetails, false, username, authenticationId);
    LOG.debug("Create a new AccessToken: {}", accessToken);
    return accessToken;
  }


  public OauthCode loadOauthCode(String code, ClientDetails clientDetails) {
    final String clientId = clientDetails.getClientId();
    return oauthRepository.findOauthCode(code, clientId);
  }


  public boolean removeOauthCode(String code, ClientDetails clientDetails) {
    final OauthCode oauthCode = loadOauthCode(code, clientDetails);
    final int rows = oauthRepository.deleteOauthCode(oauthCode);
    return rows > 0;
  }


  public AccessToken retrieveAuthorizationCodeAccessToken(ClientDetails clientDetails, String code)
      throws OAuthSystemException {
    final OauthCode oauthCode = loadOauthCode(code, clientDetails);
    final String username = oauthCode.username();
    final String clientId = clientDetails.getClientId();
    final String authenticationId = authenticationIdGenerator.generate(clientId, username, null);
    AccessToken accessToken = oauthRepository.findAccessToken(clientId, username, authenticationId);
    if (accessToken != null) {
      LOG.debug("Delete existed AccessToken: {}", accessToken);
      oauthRepository.deleteAccessToken(accessToken);
    }
    accessToken = createAndSaveAccessToken(clientDetails, clientDetails.supportRefreshToken(),
        username, authenticationId);
    LOG.debug("Create a new AccessToken: {}", accessToken);

    return accessToken;
  }


  public AccessToken retrievePasswordAccessToken(ClientDetails clientDetails, Set<String> scopes,
      String username) throws OAuthSystemException {
    String scope = OAuthUtils.encodeScopes(scopes);
    final String clientId = clientDetails.getClientId();
    final String authenticationId = authenticationIdGenerator.generate(clientId, username, scope);
    AccessToken accessToken = oauthRepository.findAccessToken(clientId, username, authenticationId);
    boolean needCreate = false;
    if (accessToken == null) {
      needCreate = true;
      LOG.debug("Not found AccessToken from repository, will create a new one, client_id: {}",
          clientId);
    } else if (accessToken.tokenExpired()) {
      LOG.debug("Delete expired AccessToken: {} and create a new one, client_id: {}", accessToken,
          clientId);
      oauthRepository.deleteAccessToken(accessToken);
      needCreate = true;
    } else {
      LOG.debug("Use existed AccessToken: {}, client_id: {}", accessToken, clientId);
    }
    if (needCreate) {
      accessToken = createAndSaveAccessToken(clientDetails, clientDetails.supportRefreshToken(),
          username, authenticationId);
      LOG.debug("Create a new AccessToken: {}", accessToken);
    }
    return accessToken;

  }


  public AccessToken retrieveClientCredentialsAccessToken(ClientDetails clientDetails,
      Set<String> scopes) throws OAuthSystemException {
    String scope = OAuthUtils.encodeScopes(scopes);
    final String clientId = clientDetails.getClientId();
    final String authenticationId = authenticationIdGenerator.generate(clientId, clientId, scope);
    AccessToken accessToken = oauthRepository.findAccessToken(clientId, clientId, authenticationId);
    boolean needCreate = false;
    if (accessToken == null) {
      needCreate = true;
      LOG.debug("Not found AccessToken from repository, will create a new one, client_id: {}",
          clientId);
    } else if (accessToken.tokenExpired()) {
      LOG.debug("Delete expired AccessToken: {} and create a new one, client_id: {}", accessToken,
          clientId);
      oauthRepository.deleteAccessToken(accessToken);
      needCreate = true;
    } else {
      LOG.debug("Use existed AccessToken: {}, client_id: {}", accessToken, clientId);
    }
    if (needCreate) {
      accessToken = createAndSaveAccessToken(clientDetails, false, clientId, authenticationId);
      LOG.debug("Create a new AccessToken: {}", accessToken);
    }
    return accessToken;
  }


  public AccessToken loadAccessTokenByRefreshToken(String refreshToken, String clientId) {
    LOG.debug("Load ClientDetails by refreshToken: {} and clientId: {}", refreshToken, clientId);
    return oauthRepository.findAccessTokenByRefreshToken(refreshToken, clientId);
  }


  public AccessToken changeAccessTokenByRefreshToken(String refreshToken, String clientId)
      throws OAuthSystemException {
    final AccessToken oldToken = loadAccessTokenByRefreshToken(refreshToken, clientId);
    AccessToken newAccessToken = oldToken.cloneMe();
    LOG.debug("Create new AccessToken: {} from old AccessToken: {}", newAccessToken, oldToken);
    ClientDetails details = oauthRepository.findClientDetails(clientId);
    newAccessToken.updateByClientDetails(details);
    final String authId = authenticationIdGenerator.generate(clientId, oldToken.username(), null);
    newAccessToken.authenticationId(authId).tokenId(oAuthIssuer.accessToken())
        .refreshToken(oAuthIssuer.refreshToken());
    oauthRepository.deleteAccessToken(oldToken);
    LOG.debug("Delete old AccessToken: {}", oldToken);
    oauthRepository.saveAccessToken(newAccessToken);
    LOG.debug("Save new AccessToken: {}", newAccessToken);
    return newAccessToken;
  }


  public boolean isExistedClientId(String clientId) {
    final ClientDetails clientDetails = loadClientDetails(clientId);
    return clientDetails != null;
  }

  private AccessToken createAndSaveAccessToken(ClientDetails clientDetails,
      boolean includeRefreshToken, String username, String authenticationId)
      throws OAuthSystemException {
    AccessToken accessToken = new AccessToken().clientId(clientDetails.getClientId())
        .username(username).tokenId(oAuthIssuer.accessToken()).authenticationId(authenticationId)
        .updateByClientDetails(clientDetails);

    if (includeRefreshToken) {
      accessToken.refreshToken(oAuthIssuer.refreshToken());
    }

    this.oauthRepository.saveAccessToken(accessToken);
    LOG.debug("Save AccessToken: {}", accessToken);
    return accessToken;
  }

  private OauthCode createOauthCode(ClientDetails clientDetails) throws OAuthSystemException {
    OauthCode oauthCode;
    final String authCode = oAuthIssuer.authorizationCode();

    LOG.debug("Save authorizationCode '{}' of ClientDetails '{}'", authCode, clientDetails);
    oauthCode = this.saveAuthorizationCode(authCode, clientDetails);
    return oauthCode;
  }

}
