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

/**
 * @author liushiming
 * @version ApiSpringCloudDO.java, v 0.0.1 2018年4月11日 下午5:49:18 liushiming
 */
public class ApiSpringCloudDO implements Serializable {
  private static final long serialVersionUID = 4715218350028915340L;

  private Long id;

  private String instanceId;

  private ApiDO api;

  private Timestamp gmtCreate;

  private Timestamp gmtModified;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public ApiDO getApi() {
    return api;
  }

  public void setApi(ApiDO api) {
    this.api = api;
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

  public ApiSpringCloudDO copy(ApiSpringCloudDO source) {
    ApiSpringCloudDO target = new ApiSpringCloudDO();
    BeanUtils.copyProperties(source, target);
    return target;
  }

}
