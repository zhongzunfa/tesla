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

import io.github.tesla.common.RequestFilterTypeEnum;
import io.github.tesla.common.domain.ApiRpcDO;
import io.github.tesla.gateway.cache.ApiAndFilterCacheComponent;
import io.github.tesla.gateway.config.SpringContextHolder;
import io.github.tesla.gateway.netty.servlet.NettyHttpServletRequest;
import io.github.tesla.gateway.protocol.grpc.DynamicGrpcClient;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * gRpc协议转换
 */
public class GrpcTransformHttpRequestFilter extends HttpRequestFilter {

  private final DynamicGrpcClient grpcClient = SpringContextHolder.getBean(DynamicGrpcClient.class);

  private final ApiAndFilterCacheComponent routeRuleCache =
      SpringContextHolder.getBean(ApiAndFilterCacheComponent.class);



  @Override
  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpObject httpObject) {
    String uri = servletRequest.getRequestURI();
    int index = uri.indexOf("?");
    if (index > -1) {
      uri = uri.substring(0, index);
    }
    ApiRpcDO rpc = routeRuleCache.getRpcRoute(uri);
    if (rpc != null && rpc.getProtoContext() != null && grpcClient != null) {
      String jsonOutput = grpcClient.doRpcRemoteCall(rpc, servletRequest);
      HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
          HttpResponseStatus.OK, Unpooled.wrappedBuffer(jsonOutput.getBytes(CharsetUtil.UTF_8)));
      HttpUtil.setKeepAlive(response, false);
      return response;
    } else {
      // 如果从缓存没有查到grpc的映射信息，说明不是泛化调用，返回空，继续走下一个filter或者去走rest服务发现等
      return null;
    }
  }

  @Override
  public RequestFilterTypeEnum filterType() {
    return RequestFilterTypeEnum.GRPC;
  }

}
