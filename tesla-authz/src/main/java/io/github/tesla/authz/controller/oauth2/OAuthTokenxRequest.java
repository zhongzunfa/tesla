package io.github.tesla.authz.controller.oauth2;

import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import javax.servlet.http.HttpServletRequest;


public class OAuthTokenxRequest extends OAuthTokenRequest {


  public OAuthTokenxRequest(HttpServletRequest request)
      throws OAuthSystemException, OAuthProblemException {
    super(request);
  }

  public HttpServletRequest request() {
    return this.request;
  }
}
