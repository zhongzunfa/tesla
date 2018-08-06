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
package io.github.tesla.gateway.filter.request;

import org.apache.commons.lang3.StringUtils;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import io.github.tesla.gateway.filter.BodyMapping;
import io.github.tesla.gateway.filter.DroolsContext;
import io.github.tesla.gateway.filter.HeaderMapping;
import io.github.tesla.gateway.filter.UserRequestFilter;
import io.github.tesla.gateway.filter.common.FilterUtil;
import io.github.tesla.gateway.filter.common.ProxyUtils;
import io.github.tesla.gateway.filter.servlet.NettyHttpServletRequest;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * @author liushiming
 * @version DroolsRequestFilter.java, v 0.0.1 2018年5月2日 下午3:08:17 liushiming
 */
public class DroolsRequestFilter extends UserRequestFilter {

  private final KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();

  @Override
  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpObject httpObject) {
    final HttpRequest nettyRequst = servletRequest.getNettyRequest();
    String uri = servletRequest.getRequestURI();
    int index = uri.indexOf("?");
    if (index > -1) {
      uri = uri.substring(0, index);
    }
    StatefulKnowledgeSession kSession = null;
    if (userRule != null) {
      String droolsDrlRule = userRule;
      try {
        kb.add(ResourceFactory.newByteArrayResource(droolsDrlRule.getBytes("utf-8")),
            ResourceType.DRL);
        KnowledgeBuilderErrors errors = kb.getErrors();
        String errorstr = null;
        for (KnowledgeBuilderError error : errors) {
          errorstr = errorstr + error.getMessage() + "\n";
        }
        if (errorstr != null) {
          throw new java.lang.IllegalArgumentException(errorstr);
        }
        KnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
        kBase.addKnowledgePackages(kb.getKnowledgePackages());
        kSession = kBase.newStatefulKnowledgeSession();
        DroolsContext content = new DroolsContext(super.getSpringCloudDiscovery());
        kSession.insert(new HeaderMapping(servletRequest));
        kSession.insert(new BodyMapping(servletRequest));
        kSession.insert(content);
        kSession.fireAllRules();
        String targetUrl = content.getTargetUrl();
        String response = content.getResponse();
        // reset url
        final FullHttpRequest realRequest = (FullHttpRequest) httpObject;
        if (targetUrl != null) {
          String requestUrl = ProxyUtils.parseUrl(targetUrl);
          realRequest.setUri(requestUrl);
          String hostAndPort = ProxyUtils.parseHostAndPort(targetUrl);
          if (!StringUtils.isBlank(hostAndPort)) {
            realRequest.headers().set(HttpHeaderNames.HOST, hostAndPort);
          }
        } else if (response != null) {
          HttpResponse httpresponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
              HttpResponseStatus.OK, Unpooled.wrappedBuffer(response.getBytes(CharsetUtil.UTF_8)));
          HttpUtil.setKeepAlive(httpresponse, false);
          return httpresponse;
        }
      } catch (Throwable e) {
        FilterUtil.writeFilterLog(DroolsRequestFilter.class, droolsDrlRule + " is error ", e);
        return FilterUtil.createResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR, nettyRequst,
            e.getMessage());
      } finally {
        if (kSession != null)
          kSession.dispose();
      }
    }
    return null;
  }

}
