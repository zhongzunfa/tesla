package io.github.tesla.authz.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.tesla.authz.controller.oauth2.OAuthAuthxRequest;
import io.github.tesla.authz.controller.oauth2.WebUtils;
import io.github.tesla.authz.controller.oauth2.authorize.CodeAuthorizeHandler;
import io.github.tesla.authz.controller.oauth2.authorize.TokenAuthorizeHandler;


@Controller
@RequestMapping("oauth/")
public class OauthAuthorizeController {


  private static final Logger LOG = LoggerFactory.getLogger(OauthAuthorizeController.class);


  @RequestMapping("authorize")
  public void authorize(HttpServletRequest request, HttpServletResponse response)
      throws OAuthSystemException, ServletException, IOException {
    try {
      OAuthAuthxRequest oauthRequest = new OAuthAuthxRequest(request);
      if (oauthRequest.isCode()) {
        CodeAuthorizeHandler codeAuthorizeHandler =
            new CodeAuthorizeHandler(oauthRequest, response);
        LOG.debug("Go to  response_type = 'code' handler: {}", codeAuthorizeHandler);
        codeAuthorizeHandler.handle();
      } else if (oauthRequest.isToken()) {
        TokenAuthorizeHandler tokenAuthorizeHandler =
            new TokenAuthorizeHandler(oauthRequest, response);
        LOG.debug("Go to response_type = 'token' handler: {}", tokenAuthorizeHandler);
        tokenAuthorizeHandler.handle();
      } else {
        unsupportResponseType(oauthRequest, response);
      }

    } catch (OAuthProblemException e) {
      LOG.debug(e.getMessage(), e);
      OAuthResponse oAuthResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
          .location(e.getRedirectUri()).error(e).buildJSONMessage();
      WebUtils.writeOAuthJsonResponse(response, oAuthResponse);
    }

  }


  private void unsupportResponseType(OAuthAuthxRequest oauthRequest, HttpServletResponse response)
      throws OAuthSystemException {
    final String responseType = oauthRequest.getResponseType();
    LOG.debug("Unsupport response_type '{}' by client_id '{}'", responseType,
        oauthRequest.getClientId());
    OAuthResponse oAuthResponse = OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
        .setError(OAuthError.CodeResponse.UNSUPPORTED_RESPONSE_TYPE)
        .setErrorDescription("Unsupport response_type '" + responseType + "'").buildJSONMessage();
    WebUtils.writeOAuthJsonResponse(response, oAuthResponse);
  }


}
