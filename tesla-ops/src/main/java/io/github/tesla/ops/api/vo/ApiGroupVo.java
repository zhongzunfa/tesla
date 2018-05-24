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
package io.github.tesla.ops.api.vo;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.beans.BeanUtils;

import io.github.tesla.common.domain.ApiGroupDO;


public class ApiGroupVo implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long id;

  private String name;

  private String describe;

  private String backendHost;

  private String backendPort;

  private String backendPath;

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

  public String getBackendHost() {
    return backendHost;
  }

  public void setBackendHost(String backendHost) {
    this.backendHost = backendHost;
  }

  public String getBackendPort() {
    return backendPort;
  }

  public void setBackendPort(String backendPort) {
    this.backendPort = backendPort;
  }

  public String getBackendPath() {
    return backendPath;
  }

  public void setBackendPath(String backendPath) {
    this.backendPath = backendPath;
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

  public static ApiGroupVo buildGroupVo(ApiGroupDO groupDo) {
    ApiGroupVo groupVo = new ApiGroupVo();
    BeanUtils.copyProperties(groupDo, groupVo);
    return groupVo;
  }

  public static ApiGroupDO buildGroupDo(ApiGroupVo groupVo) {
    ApiGroupDO groupDo = new ApiGroupDO();
    BeanUtils.copyProperties(groupVo, groupDo);
    return groupDo;
  }



}
