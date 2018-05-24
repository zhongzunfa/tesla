package io.github.tesla.authz.controller.oauth2.validator;

import java.util.Set;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.tesla.authz.controller.oauth2.OAuthTokenxRequest;
import io.github.tesla.authz.domain.ClientDetails;


public class ClientCredentialsClientDetailsValidator extends AbstractOauthTokenValidator {

  private static final Logger LOG =
      LoggerFactory.getLogger(ClientCredentialsClientDetailsValidator.class);


  public ClientCredentialsClientDetailsValidator(OAuthTokenxRequest oauthRequest) {
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
    final Set<String> scopes = oauthRequest.getScopes();
    if (scopes.isEmpty() || excludeScopes(scopes, clientDetails)) {
      return invalidScopeResponse();
    }
    return null;
  }


}
