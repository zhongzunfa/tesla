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
import io.github.tesla.common.ResponseFilterTypeEnum;
import io.github.tesla.gateway.netty.filter.help.BodyMapping;
import io.github.tesla.gateway.netty.servlet.NettyHttpServletRequest;
import io.github.tesla.gateway.utils.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

/**
 * @author liushiming
 * @version DataMappingHttpResponseFilter.java, v 0.0.1 2018年4月25日 下午4:21:53 liushiming
 */
public class DataMappingHttpResponseFilter extends HttpResponseFilter {
  private static final StringTemplateLoader templateHolder = new StringTemplateLoader();
  private static final Configuration configuration;

  static {
    Configuration config = new Configuration(Configuration.VERSION_2_3_26);
    config.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_26));
    config.setOutputFormat(JSONOutputFormat.INSTANCE);
    config.setTemplateLoader(templateHolder);
    DefaultObjectWrapperBuilder owb = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_26);
    owb.setIterableSupport(true);
    config.setObjectWrapper(owb.build());
    configuration = config;
  }

  @Override
  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpResponse httpResponse) {
    if (httpResponse instanceof FullHttpResponse) {
      FullHttpResponse fullHttpResonse = (FullHttpResponse) httpResponse;
      String uri = servletRequest.getRequestURI();
      int index = uri.indexOf("?");
      if (index > -1) {
        uri = uri.substring(0, index);
      }
      ByteBuf responseBuffer = fullHttpResonse.content();
      Boolean canDataMapping = isCanDataMapping(responseBuffer);
      if (canDataMapping) {
        Map<String, Set<String>> rules = super.getUrlRule(DataMappingHttpResponseFilter.this);
        Set<String> urlRules = rules.get(uri);
        if (urlRules != null && urlRules.size() == 1) {
          String tempalteContent = urlRules.iterator().next();
          try {
            templateHolder.putTemplate("template" + uri, tempalteContent);
            Map<String, Object> templateContext = new HashMap<String, Object>();
            templateContext.put("input", new BodyMapping(responseBuffer));
            Template template = configuration.getTemplate("template" + uri);
            StringWriter transformedWriter = new StringWriter();
            template.process(templateContext, transformedWriter);
            String transformedJson = transformedWriter.toString();
            ByteBuf bodyContent = Unpooled.copiedBuffer(transformedJson, CharsetUtil.UTF_8);
            responseBuffer.clear().writeBytes(bodyContent);
          } catch (Throwable e) {
            return super.createResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR,
                servletRequest.getNettyRequest(), "DataMapping Error");
          }
        }
      }
    }
    return httpResponse;
  }

  private Boolean isCanDataMapping(ByteBuf contentBuf) {
    try {
      String contentStr = contentBuf.toString(CharsetUtil.UTF_8);
      JsonUtils.parse(contentStr);
      return true;
    } catch (Throwable e) {
      return false;
    }
  }

  @Override
  public ResponseFilterTypeEnum filterType() {
    return ResponseFilterTypeEnum.DataMappingHttpResponseFilter;
  }

}
