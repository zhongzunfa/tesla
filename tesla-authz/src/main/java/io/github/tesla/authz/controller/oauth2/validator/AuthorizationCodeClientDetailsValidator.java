package io.github.tesla.authz.controller.oauth2.validator;

import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.tesla.authz.controller.oauth2.OAuthTokenxRequest;
import io.github.tesla.authz.domain.ClientDetails;
import io.github.tesla.authz.domain.OauthCode;


public class AuthorizationCodeClientDetailsValidator extends AbstractOauthTokenValidator {

  private static final Logger LOG =
      LoggerFactory.getLogger(AuthorizationCodeClientDetailsValidator.class);


  public AuthorizationCodeClientDetailsValidator(OAuthTokenxRequest oauthRequest) {
    super(oauthRequest);
  }


  @Override
  protected OAuthResponse validateSelf(ClientDetails clientDetails) throws OAuthSystemException {
    final String grantType = grantType();
    if (!clientDetails.grantTypes().contains(grantType)) {
      LOG.debug("Invalid grant_type '{}', client_id = '{}'", grantType,
          clientDetails.getClientId());
      return invalidGrantTypeResponse(grantType);
    }
    final String clientSecret = oauthRequest.getClientSecret();
    if (clientSecret == null || !clientSecret.equals(clientDetails.getClientSecret())) {
      LOG.debug("Invalid client_secret '{}', client_id = '{}'", clientSecret,
          clientDetails.getClientId());
      return invalidClientSecretResponse();
    }
    final String redirectURI = oauthRequest.getRedirectURI();
    if (redirectURI == null || !redirectURI.equals(clientDetails.getRedirectUri())) {
      LOG.debug("Invalid redirect_uri '{}', client_id = '{}'", redirectURI,
          clientDetails.getClientId());
      return invalidRedirectUriResponse();
    }
    String code = getCode();
    OauthCode oauthCode = oauthService.loadOauthCode(code, clientDetails());
    if (oauthCode == null) {
      LOG.debug("Invalid code '{}', client_id = '{}'", code, clientDetails.getClientId());
      return invalidCodeResponse(code);
    }
    return null;
  }

  private OAuthResponse invalidCodeResponse(String code) throws OAuthSystemException {
    return OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
        .setError(OAuthError.TokenResponse.INVALID_GRANT)
        .setErrorDescription("Invalid code '" + code + "'").buildJSONMessage();
  }

  private String getCode() {
    return ((OAuthTokenxRequest) oauthRequest).getCode();
  }
}
