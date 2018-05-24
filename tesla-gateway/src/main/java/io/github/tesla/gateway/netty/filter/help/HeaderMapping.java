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
package io.github.tesla.gateway.netty.filter.help;

import java.util.Enumeration;
import java.util.Map;

import com.google.common.collect.Maps;

import io.github.tesla.gateway.netty.servlet.NettyHttpServletRequest;

/**
 * @author liushiming
 * @version MappingContext.java, v 0.0.1 2018年4月23日 下午6:27:51 liushiming
 */
public class HeaderMapping {

  private final Map<String, String> headers = Maps.newHashMap();

  private String method;

  private String uri;

  public HeaderMapping(NettyHttpServletRequest request) {
    Enumeration<String> e = request.getHeaderNames();
    while (e.hasMoreElements()) {
      String headerName = e.nextElement();
      Enumeration<String> headerValues = request.getHeaders(headerName);
      while (headerValues.hasMoreElements()) {
        headers.put(headerName, headerValues.nextElement());
      }
    }
    this.method = request.getMethod();
    this.uri = request.getRequestURI();
  }

  public String get(String headerKey) {
    return headers.get(headerKey);
  }

  public String method() {
    return this.method;
  }

  public String uri() {
    return this.uri;
  }

}
