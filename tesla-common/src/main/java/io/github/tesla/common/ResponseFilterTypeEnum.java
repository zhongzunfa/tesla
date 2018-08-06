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
package io.github.tesla.common;

/**
 * @author liushiming
 * @version ResponseFilterOrder.java, v 0.0.1 2018年1月26日 下午4:58:38 liushiming
 */
public enum ResponseFilterTypeEnum {


  /**
   * 各种限制
   */
  UserDefinitionResponseFilter(0, "自定义出请求"), //
  ClickjackHttpResponseFilter(1, "Clickjack"), //
  XssHttpRequestFilter(2, "xss防注入攻击");

  private int filterOrder;
  private String filterViewName;

  ResponseFilterTypeEnum(int filterOrder, String filterViewName) {
    this.filterOrder = filterOrder;
    this.filterViewName = filterViewName;
  }

  public int order() {
    return filterOrder;
  }

  public String filterViewName() {
    return filterViewName;
  }

  public static ResponseFilterTypeEnum fromTypeName(String typeName) {
    for (ResponseFilterTypeEnum type : ResponseFilterTypeEnum.values()) {
      if (type.name().equals(typeName)) {
        return type;
      }
    }
    return null;
  }
}
