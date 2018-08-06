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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.tesla.common.RequestFilterTypeEnum;
import io.github.tesla.gateway.filter.servlet.NettyHttpServletRequest;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * ip黑名单过滤
 */
public class BlackIpHttpRequesFilter extends HttpRequestFilter {


  @Override
  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpObject httpObject) {
    final HttpRequest nettyRequst = servletRequest.getNettyRequest();
    String realIp = servletRequest.getHeader("X-Real-IP");
    if (realIp != null) {
      List<Pattern> patterns = super.getCommonRule(this);
      for (Pattern pattern : patterns) {
        Matcher matcher = pattern.matcher(realIp.toLowerCase());
        if (matcher.find()) {
          super.writeFilterLog(BlackIpHttpRequesFilter.class,
              realIp + " match " + pattern.pattern());
          return super.createResponse(HttpResponseStatus.FORBIDDEN, nettyRequst);
        }
      }
    }
    return null;
  }

  @Override
  public RequestFilterTypeEnum filterType() {
    return RequestFilterTypeEnum.BlackIpHttpRequesFilter;
  }



}
