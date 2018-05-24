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
package io.github.tesla.gateway.netty.filter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import io.github.tesla.gateway.cache.ApiAndFilterCacheComponent;
import io.github.tesla.gateway.config.SpringContextHolder;
import io.github.tesla.gateway.netty.filter.request.BlackCookieHttpRequestFilter;
import io.github.tesla.gateway.netty.filter.request.BlackURLHttpRequestFilter;
import io.github.tesla.gateway.netty.filter.request.HttpRequestFilter;
import io.github.tesla.gateway.netty.filter.request.URLParamHttpRequestFilter;
import io.github.tesla.gateway.utils.FilterUtil;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * @author liushiming
 * @version AbstractCommonFilter.java, v 0.0.1 2018年4月25日 下午4:43:50 liushiming
 */
public abstract class AbstractCommonFilter {

  private static final Logger logger = LoggerFactory.getLogger("ProxyFilterLog");

  protected static final PathMatcher pathMatcher = new AntPathMatcher();

  private static final String LINE_SEPARATOR_UNIX = "\n";

  private static final String LINE_SEPARATOR_WINDOWS = "\r\n";

  public abstract String filterName();

  protected List<Pattern> getCommonRule(HttpRequestFilter filterClazz) {
    ApiAndFilterCacheComponent ruleCache =
        SpringContextHolder.getBean(ApiAndFilterCacheComponent.class);
    Set<Pattern> compilePatterns = Sets.newHashSet();
    Set<String> rules = ruleCache.getPubicFilterRule(filterClazz);
    for (String rule : rules) {
      String[] rulesSplits = new String[] {rule};
      if (filterClazz instanceof BlackCookieHttpRequestFilter
          || filterClazz instanceof URLParamHttpRequestFilter
          || filterClazz instanceof BlackURLHttpRequestFilter) {
        if (StringUtils.contains(rule, LINE_SEPARATOR_UNIX)) {
          rulesSplits = StringUtils.split(rule, LINE_SEPARATOR_UNIX);
        } else if (StringUtils.contains(rule, LINE_SEPARATOR_WINDOWS)) {
          rulesSplits = StringUtils.split(rule, LINE_SEPARATOR_UNIX);
        }
      }
      for (String rulesSplit : rulesSplits) {
        try {
          Pattern compilePattern = Pattern.compile(rulesSplit);
          compilePatterns.add(compilePattern);
        } catch (Throwable e) {
          e.printStackTrace();
        }
      }
    }
    return Lists.newArrayList(compilePatterns);
  }

  protected Map<String, Set<String>> getUrlRule(AbstractCommonFilter filterClazz) {
    ApiAndFilterCacheComponent ruleCache =
        SpringContextHolder.getBean(ApiAndFilterCacheComponent.class);
    Map<String, Set<String>> rules = ruleCache.getUrlFilterRule(filterClazz);
    return rules;
  }

  protected HttpResponse createResponse(HttpResponseStatus httpResponseStatus,
      HttpRequest originalRequest, String... reason) {
    HttpHeaders httpHeaders = new DefaultHttpHeaders();
    httpHeaders.add("Transfer-Encoding", "chunked");
    HttpResponse httpResponse;
    if (reason != null) {
      httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpResponseStatus,
          Unpooled.copiedBuffer(Arrays.toString(reason), CharsetUtil.UTF_8));
    } else {
      httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpResponseStatus);
    }
    List<String> originHeader = FilterUtil.getHeaderValues(originalRequest, "Origin");
    if (originHeader.size() > 0) {
      httpHeaders.set("Access-Control-Allow-Credentials", "true");
      httpHeaders.set("Access-Control-Allow-Origin", originHeader.get(0));
    }
    httpResponse.headers().add(httpHeaders);
    return httpResponse;
  }

  protected void writeFilterLog(Class<?> type, String reason, Throwable... cause) {
    if (cause != null) {
      logger.error("execute filter:" + type + " occur error, reason is:" + reason, cause[0]);
    } else {
      logger.info("execute filter:" + type + " occur error, reason is:" + reason);
    }
  }
}
