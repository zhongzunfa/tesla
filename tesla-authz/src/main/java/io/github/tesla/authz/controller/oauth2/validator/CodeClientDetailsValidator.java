package io.github.tesla.authz.controller.oauth2.validator;

import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.tesla.authz.domain.ClientDetails;


public class CodeClientDetailsValidator extends AbstractClientDetailsValidator {

  private static final Logger LOG = LoggerFactory.getLogger(CodeClientDetailsValidator.class);

  public CodeClientDetailsValidator(OAuthAuthzRequest oauthRequest) {
    super(oauthRequest);
  }


  @Override
  public OAuthResponse validateSelf(ClientDetails clientDetails) throws OAuthSystemException {
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
    final String state = getState();
    if (StringUtils.isEmpty(state)) {
      LOG.debug("Invalid 'state', it is required, but it is empty");
      return invalidStateResponse();
    }
    return null;
  }

  private String getState() {
    return ((OAuthAuthzRequest) oauthRequest).getState();
  }

  private OAuthResponse invalidStateResponse() throws OAuthSystemException {
    return OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
        .setError(OAuthError.CodeResponse.INVALID_REQUEST)
        .setErrorDescription("Parameter 'state'  is required").buildJSONMessage();
  }


}
