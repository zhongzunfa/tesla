package io.github.tesla.authz.controller.oauth2.validator;

import java.util.Set;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.tesla.authz.controller.oauth2.OAuthTokenxRequest;
import io.github.tesla.authz.domain.ClientDetails;


public class PasswordClientDetailsValidator extends AbstractOauthTokenValidator {

  private static final Logger LOG = LoggerFactory.getLogger(PasswordClientDetailsValidator.class);


  public PasswordClientDetailsValidator(OAuthTokenxRequest oauthRequest) {
    super(oauthRequest);
  }


  @Override
  protected OAuthResponse validateSelf(ClientDetails clientDetails) throws OAuthSystemException {
    final String grantType = grantType();
    if (invalidateGrantType(clientDetails, grantType)) {
      return invalidGrantTypeResponse(grantType);
    }
    if (invalidateClientSecret(clientDetails)) {
      return invalidClientSecretResponse();
    }
    if (invalidateScope(clientDetails)) {
      return invalidScopeResponse();
    }
    if (invalidUsernamePassword()) {
      return invalidUsernamePasswordResponse();
    }

    return null;
  }

  private boolean invalidateGrantType(ClientDetails clientDetails, String grantType)
      throws OAuthSystemException {
    if (!clientDetails.grantTypes().contains(grantType)) {
      LOG.debug("Invalid grant_type '{}', client_id = '{}'", grantType,
          clientDetails.getClientId());
      return true;
    }
    return false;
  }

  private boolean invalidateScope(ClientDetails clientDetails) throws OAuthSystemException {
    final Set<String> scopes = oauthRequest.getScopes();
    return scopes.isEmpty() || excludeScopes(scopes, clientDetails);
  }

  private boolean invalidateClientSecret(ClientDetails clientDetails) throws OAuthSystemException {
    final String clientSecret = oauthRequest.getClientSecret();
    if (clientSecret == null || !clientSecret.equals(clientDetails.getClientSecret())) {
      LOG.debug("Invalid client_secret '{}', client_id = '{}'", clientSecret,
          clientDetails.getClientId());
      return true;
    }
    return false;
  }


}
