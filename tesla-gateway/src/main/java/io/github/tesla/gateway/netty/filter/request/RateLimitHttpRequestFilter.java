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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.RateLimiter;

import io.github.tesla.common.RequestFilterTypeEnum;
import io.github.tesla.gateway.netty.servlet.NettyHttpServletRequest;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * 限流
 */
public class RateLimitHttpRequestFilter extends HttpRequestFilter {

  private final LoadingCache<String, RateLimiter> loadingCache =
      CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(2, TimeUnit.SECONDS)
          .build(new CacheLoader<String, RateLimiter>() {
            @Override
            public RateLimiter load(String key) throws Exception {
              Map<String, Set<String>> limiter =
                  RateLimitHttpRequestFilter.this.getUrlRule(RateLimitHttpRequestFilter.this);
              List<String> limitValue = Lists.newArrayList(limiter.get(key));
              if (limitValue != null) {
                Double limitValueMax =
                    Collections.max(Lists.transform(limitValue, new Function<String, Double>() {

                      @Override
                      public Double apply(String input) {
                        return Double.valueOf(input);
                      }

                    }));
                RateLimiter rateLimiter = RateLimiter.create(limitValueMax);
                return rateLimiter;
              } else {
                return null;
              }

            }
          });

  @Override
  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpObject httpObject) {
    String uri = servletRequest.getRequestURI();
    int index = uri.indexOf("?");
    if (index > -1) {
      uri = uri.substring(0, index);
    }
    RateLimiter rateLimiter = null;
    try {
      rateLimiter = loadingCache.get(uri);
    } catch (Throwable e) {
    }
    // 如果1秒钟没有获取令牌，说明被限制了
    if (rateLimiter != null && !rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
      super.writeFilterLog(RateLimitHttpRequestFilter.class,
          servletRequest.getRequestURI() + " Ratelimit Filter has limited");
      final HttpRequest nettyRequst = servletRequest.getNettyRequest();
      return super.createResponse(HttpResponseStatus.TOO_MANY_REQUESTS, nettyRequst);
    }
    return null;
  }

  @Override
  public RequestFilterTypeEnum filterType() {
    return RequestFilterTypeEnum.RateLimitHttpRequestFilter;
  }

}
