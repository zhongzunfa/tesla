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

import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.tesla.common.RequestFilterTypeEnum;
import io.github.tesla.gateway.netty.servlet.NettyHttpServletRequest;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * user-agent黑名单过滤
 */
public class BlackUaHttpRequestFilter extends HttpRequestFilter {


  @Override
  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpObject httpObject) {
    final HttpRequest nettyRequst = servletRequest.getNettyRequest();
    Enumeration<String> userAgents = servletRequest.getHeaders("User-Agent");
    List<Pattern> patterns = super.getCommonRule(this);
    while (userAgents.hasMoreElements()) {
      String userAgent = userAgents.nextElement();
      for (Pattern pattern : patterns) {
        Matcher matcher = pattern.matcher(userAgent);
        if (matcher.find()) {
          super.writeFilterLog(BlackUaHttpRequestFilter.class,
              userAgent + " match " + pattern.pattern());
          return super.createResponse(HttpResponseStatus.FORBIDDEN, nettyRequst);
        }
      }
    }
    return null;
  }

  @Override
  public RequestFilterTypeEnum filterType() {
    return RequestFilterTypeEnum.BlackUaHttpRequestFilter;
  }

}
