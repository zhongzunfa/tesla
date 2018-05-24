package io.github.tesla.authz.controller.oauth2.token;

import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.tesla.authz.controller.oauth2.OAuthHandler;
import io.github.tesla.authz.controller.oauth2.OAuthTokenxRequest;
import io.github.tesla.authz.controller.oauth2.WebUtils;
import io.github.tesla.authz.controller.oauth2.validator.AbstractClientDetailsValidator;


public abstract class AbstractOAuthTokenHandler extends OAuthHandler implements OAuthTokenHandler {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractOAuthTokenHandler.class);

  protected OAuthTokenxRequest tokenRequest;

  protected HttpServletResponse response;

  @Override
  public final void handle(OAuthTokenxRequest tokenRequest, HttpServletResponse response)
      throws OAuthProblemException, OAuthSystemException {
    this.tokenRequest = tokenRequest;
    this.response = response;
    if (validateFailed()) {
      return;
    }
    handleAfterValidation();
  }


  protected boolean validateFailed() throws OAuthSystemException {
    AbstractClientDetailsValidator validator = getValidator();
    LOG.debug("Use [{}] validate client: {}", validator, tokenRequest.getClientId());

    final OAuthResponse oAuthResponse = validator.validate();
    return checkAndResponseValidateFailed(oAuthResponse);
  }

  protected boolean checkAndResponseValidateFailed(OAuthResponse oAuthResponse) {
    if (oAuthResponse != null) {
      LOG.debug("Validate OAuthAuthzRequest(client_id={}) failed", tokenRequest.getClientId());
      WebUtils.writeOAuthJsonResponse(response, oAuthResponse);
      return true;
    }
    return false;
  }

  protected String clientId() {
    return tokenRequest.getClientId();
  }


  protected abstract AbstractClientDetailsValidator getValidator();


  protected abstract void handleAfterValidation()
      throws OAuthProblemException, OAuthSystemException;


}
