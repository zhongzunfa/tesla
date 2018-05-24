/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.tesla.gateway.netty.filter.response;

import io.github.tesla.common.ResponseFilterTypeEnum;
import io.github.tesla.gateway.netty.servlet.NettyHttpServletRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import org.springframework.util.StringUtils;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import static io.github.tesla.gateway.netty.filter.help.JWTFilterConstant.*;

public class JWTSetCookieResponseFilter extends HttpResponseFilter {
  @Override
  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpResponse httpResponse) {
    String keId = (String) servletRequest.getAttribute(KE_ID_NAME);
    String uusId = (String) servletRequest.getAttribute(UUS_ID_NAME);

    if (StringUtils.isEmpty(keId) || StringUtils.isEmpty(uusId)) {
      return httpResponse;
    }

    setCookies(httpResponse, keId, uusId);
    return httpResponse;
  }

  @Override
  public ResponseFilterTypeEnum filterType() {
    return ResponseFilterTypeEnum.JWTSetCookieResponseFilter;
  }

  private void setCookies(HttpResponse response, String keId, String uusId) {
    Cookie keIdCookie = new DefaultCookie(KE_ID_COOKIE_NAME, keId);
    keIdCookie.setMaxAge(EXPIRY_SECONDS);
    keIdCookie.setDomain(DOMAIN_COOKIE_BKJK);
    response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.LAX.encode(keIdCookie));

    Cookie uusIdCookie = new DefaultCookie(UUS_ID_COOKIE_NAME, uusId);
    uusIdCookie.setMaxAge(EXPIRY_SECONDS);
    uusIdCookie.setDomain(DOMAIN_COOKIE_BKJK);
    response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.LAX.encode(uusIdCookie));
  }
}