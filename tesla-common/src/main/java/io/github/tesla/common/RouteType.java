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
 * @version RouteType.java, v 0.0.1 2018年4月23日 下午12:00:50 liushiming
 */
public enum RouteType {
  DirectRoute(0, "直接路由"), //
  DUBBO(1, "Dubbo"), //
  GRPC(2, "gRPC"), //
  SpringCloud(3, "SpringCloud"); //

  private int type;
  private String typeName;

  RouteType(int type, String typeName) {
    this.type = type;
    this.typeName = typeName;
  }

  public int type() {
    return this.type;
  }

  public String typeName() {
    return this.typeName;
  }

  public static Boolean isDubbo(RouteType type) {
    return type == RouteType.DUBBO;
  }

  public static Boolean isDubbo(Integer type) {
    return type == RouteType.DUBBO.type;
  }

  public static Boolean isGrpc(RouteType type) {
    return type == RouteType.GRPC;
  }

  public static Boolean isGrpc(Integer type) {
    return type == RouteType.GRPC.type;
  }

  public static Boolean isSpringCloud(RouteType type) {
    return type == RouteType.SpringCloud;
  }

  public static Boolean isSpringCloud(Integer type) {
    return type == RouteType.SpringCloud.type;
  }

  public static RouteType fromType(Integer type) {
    for (RouteType routeType : RouteType.values()) {
      if (routeType.type == type) {
        return routeType;
      }
    }
    return null;
  }


}
