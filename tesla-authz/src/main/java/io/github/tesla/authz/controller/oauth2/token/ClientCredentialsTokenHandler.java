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
import io.github.tesla.authz.controller.oauth2.validator.ClientCredentialsClientDetailsValidator;
import io.github.tesla.authz.domain.AccessToken;


public class ClientCredentialsTokenHandler extends AbstractOAuthTokenHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ClientCredentialsTokenHandler.class);


  @Override
  public boolean support(OAuthTokenxRequest tokenRequest) throws OAuthProblemException {
    final String grantType = tokenRequest.getGrantType();
    return GrantType.CLIENT_CREDENTIALS.toString().equalsIgnoreCase(grantType);
  }


  @Override
  public void handleAfterValidation() throws OAuthProblemException, OAuthSystemException {
    AccessToken accessToken = oauthService.retrieveClientCredentialsAccessToken(clientDetails(),
        tokenRequest.getScopes());
    final OAuthResponse tokenResponse = createTokenResponse(accessToken, false);
    LOG.debug("'client_credentials' response: {}", tokenResponse);
    WebUtils.writeOAuthJsonResponse(response, tokenResponse);
  }

  @Override
  protected AbstractClientDetailsValidator getValidator() {
    return new ClientCredentialsClientDetailsValidator(tokenRequest);
  }

}
