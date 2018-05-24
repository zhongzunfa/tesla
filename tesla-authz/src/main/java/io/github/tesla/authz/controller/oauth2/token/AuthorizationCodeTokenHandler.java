package io.github.tesla.authz.controller.oauth2.token;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.tesla.authz.controller.oauth2.OAuthTokenxRequest;
import io.github.tesla.authz.controller.oauth2.WebUtils;
import io.github.tesla.authz.controller.oauth2.validator.AbstractClientDetailsValidator;
import io.github.tesla.authz.controller.oauth2.validator.AuthorizationCodeClientDetailsValidator;
import io.github.tesla.authz.domain.AccessToken;


public class AuthorizationCodeTokenHandler extends AbstractOAuthTokenHandler {

  private static final Logger LOG = LoggerFactory.getLogger(AuthorizationCodeTokenHandler.class);

  @Override
  public boolean support(OAuthTokenxRequest tokenRequest) throws OAuthProblemException {
    final String grantType = tokenRequest.getGrantType();
    return GrantType.AUTHORIZATION_CODE.toString().equalsIgnoreCase(grantType);
  }


  @Override
  public void handleAfterValidation() throws OAuthProblemException, OAuthSystemException {
    responseToken();
    removeCode();
  }

  private void removeCode() {
    final String code = tokenRequest.getCode();
    final boolean result = oauthService.removeOauthCode(code, clientDetails());
    LOG.debug("Remove code: {} result: {}", code, result);
  }

  private void responseToken() throws OAuthSystemException {
    AccessToken accessToken =
        oauthService.retrieveAuthorizationCodeAccessToken(clientDetails(), tokenRequest.getCode());
    final OAuthResponse tokenResponse = createTokenResponse(accessToken, false);
    LOG.debug("'authorization_code' response: {}", tokenResponse);
    WebUtils.writeOAuthJsonResponse(response, tokenResponse);
  }

  @Override
  protected AbstractClientDetailsValidator getValidator() {
    return new AuthorizationCodeClientDetailsValidator(tokenRequest);
  }

}
