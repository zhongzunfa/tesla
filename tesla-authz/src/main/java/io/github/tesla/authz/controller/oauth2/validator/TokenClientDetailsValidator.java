package io.github.tesla.authz.controller.oauth2.validator;

import java.util.Set;

import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.tesla.authz.domain.ClientDetails;


public class TokenClientDetailsValidator extends AbstractClientDetailsValidator {

  private static final Logger LOG = LoggerFactory.getLogger(TokenClientDetailsValidator.class);


  private final boolean validateClientSecret;


  public TokenClientDetailsValidator(OAuthAuthzRequest oauthRequest) {
    this(oauthRequest, true);
  }

  public TokenClientDetailsValidator(OAuthAuthzRequest oauthRequest, boolean validateClientSecret) {
    super(oauthRequest);
    this.validateClientSecret = validateClientSecret;
  }


  @Override
  public OAuthResponse validateSelf(ClientDetails clientDetails) throws OAuthSystemException {
    if (this.validateClientSecret) {
      final String clientSecret = oauthRequest.getClientSecret();
      if (clientSecret == null || !clientSecret.equals(clientDetails.getClientSecret())) {
        return invalidClientSecretResponse();
      }
    }
    final String redirectURI = oauthRequest.getRedirectURI();
    if (redirectURI == null || !redirectURI.equals(clientDetails.getRedirectUri())) {
      LOG.debug("Invalid redirect_uri '{}' by response_type = 'code', client_id = '{}'",
          redirectURI, clientDetails.getClientId());
      return invalidRedirectUriResponse();
    }
    final Set<String> scopes = oauthRequest.getScopes();
    if (scopes.isEmpty() || excludeScopes(scopes, clientDetails)) {
      return invalidScopeResponse();
    }
    return null;
  }


}
