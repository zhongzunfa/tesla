package io.github.tesla.authz.controller.oauth2;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.tesla.authz.SpringContextHolder;
import io.github.tesla.authz.domain.AccessToken;
import io.github.tesla.authz.domain.ClientDetails;
import io.github.tesla.authz.service.OauthService;


public abstract class OAuthHandler {

  private static final Logger LOG = LoggerFactory.getLogger(OAuthHandler.class);

  protected transient OauthService oauthService = SpringContextHolder.getBean(OauthService.class);

  private ClientDetails clientDetails;


  protected ClientDetails clientDetails() {
    if (clientDetails == null) {
      final String clientId = clientId();
      clientDetails = oauthService.loadClientDetails(clientId);
      LOG.debug("Load ClientDetails: {} by clientId: {}", clientDetails, clientId);
    }
    return clientDetails;
  }


  protected OAuthResponse createTokenResponse(AccessToken accessToken, boolean queryOrJson)
      throws OAuthSystemException {
    final ClientDetails tempClientDetails = clientDetails();
    final OAuthASResponse.OAuthTokenResponseBuilder builder =
        OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
            .location(tempClientDetails.getRedirectUri()).setAccessToken(accessToken.tokenId())
            .setExpiresIn(String.valueOf(accessToken.currentTokenExpiredSeconds()))
            .setTokenType(accessToken.tokenType());
    final String refreshToken = accessToken.refreshToken();
    if (StringUtils.isNotEmpty(refreshToken)) {
      builder.setRefreshToken(refreshToken);
    }
    return queryOrJson ? builder.buildQueryMessage() : builder.buildJSONMessage();
  }

  protected abstract String clientId();

}
