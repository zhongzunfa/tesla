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
package io.github.tesla.gateway.netty;

import java.net.InetSocketAddress;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.tesla.gateway.cache.ApiAndFilterCacheComponent;
import io.github.tesla.gateway.config.SpringContextHolder;
import io.github.tesla.gateway.metrics.MetricsExporter;
import io.github.tesla.gateway.netty.filter.HttpRequestFilterChain;
import io.github.tesla.gateway.netty.filter.HttpResponseFilterChain;
import io.github.tesla.gateway.netty.servlet.NettyHttpServletRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * @author liushiming
 * @version HttpFiltersAdapter.java, v 0.0.1 2018年1月24日 下午3:06:25 liushiming
 */
public class HttpFiltersAdapter {
  private static Logger logger = LoggerFactory.getLogger(HttpFiltersAdapter.class);

  private final NettyHttpServletRequest serveletRequest;

  private final ChannelHandlerContext ctx;

  private final ApiAndFilterCacheComponent dynamicsRouteCache;

  private final MetricsExporter metricExporter;

  private volatile Object requestStart;

  public HttpFiltersAdapter(HttpRequest originalRequest, ChannelHandlerContext ctx,
      MetricsExporter metricExporter) {
    this.ctx = ctx;
    this.metricExporter = metricExporter;
    this.serveletRequest = new NettyHttpServletRequest((FullHttpRequest) originalRequest);
    this.dynamicsRouteCache = SpringContextHolder.getBean(ApiAndFilterCacheComponent.class);
  }


  public HttpResponse clientToProxyRequest(HttpObject httpObject) {
    requestStart =
        metricExporter.requestStart(serveletRequest.getMethod(), serveletRequest.getRequestURI());
    metricExporter.requestSize(serveletRequest.getNettyRequest().content().readableBytes());
    HttpResponse httpResponse = null;
    try {
      httpResponse =
          HttpRequestFilterChain.requestFilterChain().doFilter(serveletRequest, httpObject, ctx);
    } catch (Throwable e) {
      httpResponse =
          createResponse(HttpResponseStatus.BAD_GATEWAY, serveletRequest.getNettyRequest());
      logger.error("Client connectTo proxy request failed", e);
    }
    return httpResponse;
  }

  public HttpObject proxyToClientResponse(HttpObject httpObject) {
    if (httpObject instanceof HttpResponse) {
      HttpResponse response = (HttpResponse) httpObject;
      try {
        HttpResponseFilterChain.responseFilterChain().doFilter(serveletRequest, response);
        if (response instanceof FullHttpResponse) {
          metricExporter.responseSize(((FullHttpResponse) response).content().readableBytes());
        }
        return httpObject;
      } finally {
        metricExporter.requestEnd(serveletRequest.getMethod(), serveletRequest.getRequestURI(),
            response.status().code(), requestStart);
      }
    } else {
      return httpObject;
    }
  }

  public void proxyToServerResolutionSucceeded(String serverHostAndPort,
      InetSocketAddress resolvedRemoteAddress) {
    if (resolvedRemoteAddress == null) {
      ctx.writeAndFlush(
          createResponse(HttpResponseStatus.BAD_GATEWAY, serveletRequest.getNettyRequest()));
    }
  }

  public void dynamicsRouting(HttpRequest httpRequest) {
    String actorPath = httpRequest.uri();
    int index = actorPath.indexOf("?");
    if (index > -1) {
      actorPath = actorPath.substring(0, index);
    }
    Pair<String, String> route = dynamicsRouteCache.getDirectRoute(actorPath);
    if (route != null) {
      String targetPath = route.getRight();
      String targetHostAndPort = route.getLeft();
      if (targetHostAndPort != null)
        httpRequest.headers().set(HttpHeaderNames.HOST, targetHostAndPort);
      if (StringUtils.isNotBlank(targetPath))
        httpRequest.setUri(targetPath);
    }
  }

  private HttpResponse createResponse(HttpResponseStatus httpResponseStatus,
      HttpRequest originalRequest) {
    HttpHeaders httpHeaders = new DefaultHttpHeaders();
    httpHeaders.add("Transfer-Encoding", "chunked");
    HttpResponse httpResponse =
        new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpResponseStatus);
    httpResponse.headers().add(httpHeaders);
    return httpResponse;
  }

  public HttpResponse proxyToServerRequest(HttpObject httpObject) {
    return null;
  }

  public void proxyToServerRequestSending() {}

  public void proxyToServerRequestSent() {}

  public HttpObject serverToProxyResponse(HttpObject httpObject) {
    return httpObject;
  }

  public void serverToProxyResponseTimedOut() {}

  public void serverToProxyResponseReceiving() {}

  public void serverToProxyResponseReceived() {}

  public void proxyToServerConnectionQueued() {}


  public InetSocketAddress proxyToServerResolutionStarted(String resolvingServerHostAndPort) {
    return null;
  }

  public void proxyToServerResolutionFailed(String hostAndPort) {}

  public void proxyToServerConnectionStarted() {}

  public void proxyToServerConnectionSSLHandshakeStarted() {}

  public void proxyToServerConnectionFailed() {}

  public void proxyToServerConnectionSucceeded(ChannelHandlerContext serverCtx) {}

}
