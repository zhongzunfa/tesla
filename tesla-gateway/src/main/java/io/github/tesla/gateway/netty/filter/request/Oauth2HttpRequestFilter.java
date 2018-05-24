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
package io.github.tesla.gateway.netty.filter.request;

import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;

import io.github.tesla.common.RequestFilterTypeEnum;
import io.github.tesla.gateway.cache.Oauth2TokenCacheComponent;
import io.github.tesla.gateway.config.SpringContextHolder;
import io.github.tesla.gateway.netty.servlet.NettyHttpServletRequest;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * oauth2验证
 */
public class Oauth2HttpRequestFilter extends HttpRequestFilter {

  private final Oauth2TokenCacheComponent oauth2TokenCache =
      SpringContextHolder.getBean(Oauth2TokenCacheComponent.class);

  @Override
  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpObject httpObject) {
    try {
      OAuthAccessResourceRequest oauthRequest =
          new OAuthAccessResourceRequest(servletRequest, ParameterStyle.QUERY);
      String accessToken = oauthRequest.getAccessToken();
      if (!oauth2TokenCache.checkAccessToken(accessToken)) {
        final HttpRequest nettyRequst = servletRequest.getNettyRequest();
        return super.createResponse(HttpResponseStatus.FORBIDDEN, nettyRequst);
      }
    } catch (Throwable e) {
      return null;
    }
    return null;
  }

  @Override
  public RequestFilterTypeEnum filterType() {
    return RequestFilterTypeEnum.Oauth2HttpRequestFilter;
  }

}
