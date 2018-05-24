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
package io.github.tesla.common.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.beans.BeanUtils;

import io.github.tesla.common.RouteType;

/**
 * @author liushiming
 * @version ApiDO.java, v 0.0.1 2018年1月4日 上午10:28:15 liushiming
 */
public class ApiDO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private String name;

  private String describe;

  private Integer routes;

  private String url;

  private String httpMethod;

  private String path;

  private ApiGroupDO apiGroup;

  private Timestamp gmtCreate;

  private Timestamp gmtModified;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescribe() {
    return describe;
  }

  public void setDescribe(String describe) {
    this.describe = describe;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public void setHttpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Integer getRoutes() {
    return routes;
  }

  public void setRoutes(Integer routes) {
    this.routes = routes;
  }

  public ApiGroupDO getApiGroup() {
    return apiGroup;
  }

  public void setApiGroup(ApiGroupDO apiGroup) {
    this.apiGroup = apiGroup;
  }

  public Boolean isRpc() {
    return RouteType.isDubbo(this.routes) || RouteType.isGrpc(this.routes);
  }

  public Boolean isSpringCloud() {
    return RouteType.isSpringCloud(this.routes);
  }

  public Timestamp getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Timestamp gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Timestamp getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Timestamp gmtModified) {
    this.gmtModified = gmtModified;
  }

  public ApiDO copy() {
    ApiDO target = new ApiDO();
    BeanUtils.copyProperties(this, target);
    return target;
  }

  @Override
  public String toString() {
    return "ApiDO [id=" + id + ", name=" + name + ", describe=" + describe + ", routes=" + routes
        + ", url=" + url + ", path=" + path + ", apiGroup=" + apiGroup + ", gmtCreate=" + gmtCreate
        + ", gmtModified=" + gmtModified + "]";
  }

}
