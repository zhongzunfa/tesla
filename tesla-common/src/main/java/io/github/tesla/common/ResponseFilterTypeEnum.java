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
  JWTSetCookieResponseFilter(1), //
  ClickjackHttpResponseFilter(2), //
  DataMappingHttpResponseFilter(3);

  private int filterOrder;

  ResponseFilterTypeEnum(int filterOrder) {
    this.filterOrder = filterOrder;
  }

  public int order() {
    return filterOrder;
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
