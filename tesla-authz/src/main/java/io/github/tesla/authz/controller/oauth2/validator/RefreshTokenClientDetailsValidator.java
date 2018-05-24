package io.github.tesla.authz.controller.oauth2.validator;

import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.tesla.authz.controller.oauth2.OAuthTokenxRequest;
import io.github.tesla.authz.domain.AccessToken;
import io.github.tesla.authz.domain.ClientDetails;


public class RefreshTokenClientDetailsValidator extends AbstractOauthTokenValidator {

  private static final Logger LOG =
      LoggerFactory.getLogger(RefreshTokenClientDetailsValidator.class);


  public RefreshTokenClientDetailsValidator(OAuthTokenxRequest oauthRequest) {
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
    final String refreshToken = tokenRequest.getRefreshToken();
    AccessToken accessToken =
        oauthService.loadAccessTokenByRefreshToken(refreshToken, oauthRequest.getClientId());
    if (accessToken == null || accessToken.refreshTokenExpired()) {
      LOG.debug("Invalid refresh_token: '{}'", refreshToken);
      return invalidRefreshTokenResponse(refreshToken);
    }
    return null;
  }

  private OAuthResponse invalidRefreshTokenResponse(String refreshToken)
      throws OAuthSystemException {
    return OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
        .setError(OAuthError.TokenResponse.INVALID_GRANT)
        .setErrorDescription("Invalid refresh_token: " + refreshToken).buildJSONMessage();
  }


}
