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

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.JSONOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import io.github.tesla.common.RequestFilterTypeEnum;
import io.github.tesla.gateway.netty.filter.help.BodyMapping;
import io.github.tesla.gateway.netty.filter.help.HeaderMapping;
import io.github.tesla.gateway.netty.servlet.NettyHttpServletRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.CharsetUtil;

/**
 * @author liushiming
 * @version DataMappingRequestFilter.java, v 0.0.1 2018年4月24日 上午9:50:22 liushiming
 */
public class DataMappingRequestFilter extends HttpRequestFilter {

  private final StringTemplateLoader templateHolder = new StringTemplateLoader();

  private final Configuration configuration;

  public DataMappingRequestFilter() {
    Configuration configuration_ = new Configuration(Configuration.VERSION_2_3_26);
    configuration_.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_26));
    configuration_.setOutputFormat(JSONOutputFormat.INSTANCE);
    configuration_.setTemplateLoader(templateHolder);
    DefaultObjectWrapperBuilder owb = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_26);
    owb.setIterableSupport(true);
    configuration_.setObjectWrapper(owb.build());
    this.configuration = configuration_;
  }

  @Override
  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpObject httpObject) {
    final HttpRequest nettyRequst = servletRequest.getNettyRequest();
    String uri = servletRequest.getRequestURI();
    int index = uri.indexOf("?");
    if (index > -1) {
      uri = uri.substring(0, index);
    }
    Map<String, Set<String>> rules = super.getUrlRule(DataMappingRequestFilter.this);
    Set<String> urlRules = rules.get(uri);
    if (urlRules != null && urlRules.size() == 1) {
      String tempalteContent = urlRules.iterator().next();
      try {
        templateHolder.putTemplate("template" + uri, tempalteContent);
        Map<String, Object> templateContext = new HashMap<String, Object>();
        templateContext.put("header", new HeaderMapping(servletRequest));
        templateContext.put("input", new BodyMapping(servletRequest));
        Template template = configuration.getTemplate("template" + uri);
        StringWriter transformedWriter = new StringWriter();
        template.process(templateContext, transformedWriter);
        String transformedJson = transformedWriter.toString();
        ByteBuf bodyContent = Unpooled.copiedBuffer(transformedJson, CharsetUtil.UTF_8);
        // reset body
        final FullHttpRequest realRequest = (FullHttpRequest) httpObject;
        realRequest.content().clear().writeBytes(bodyContent);
        HttpUtil.setContentLength(realRequest, bodyContent.readerIndex());
      } catch (Throwable e) {
        super.writeFilterLog(DataMappingRequestFilter.class, tempalteContent + " is error ", e);
        return super.createResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR, nettyRequst,
            "DataMapping Error");
      }
    }
    return null;
  }



  @Override
  public RequestFilterTypeEnum filterType() {
    return RequestFilterTypeEnum.DataMappingRequestFilter;
  }

}
