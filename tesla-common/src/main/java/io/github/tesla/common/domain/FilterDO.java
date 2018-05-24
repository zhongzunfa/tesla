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

import io.github.tesla.common.RequestFilterTypeEnum;

/**
 * @author liushiming
 * @version FilterRule.java, v 0.0.1 2018年2月11日 下午2:00:13 liushiming
 */
public class FilterDO implements Serializable {

  private static final long serialVersionUID = 8473084736935164540L;

  private Long id;

  private String name;

  private String describe;

  private RequestFilterTypeEnum filterType;

  private String rule;

  private Long apiId;

  private Long groupId;

  private Timestamp gmtCreate;

  private Timestamp gmtModified;


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

  public RequestFilterTypeEnum getFilterType() {
    return filterType;
  }

  public void setFilterType(RequestFilterTypeEnum filterType) {
    this.filterType = filterType;
  }

  public void setFilterType(String filterType) {
    RequestFilterTypeEnum type = RequestFilterTypeEnum.fromTypeName(filterType);
    if (type != null) {
      this.filterType = type;
    } else {
      throw new java.lang.IllegalArgumentException(
          "no type found in defination,[" + filterType + "]");
    }
  }

  public String getRule() {
    return rule;
  }

  public void setRule(String rule) {
    this.rule = rule;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getApiId() {
    return apiId;
  }

  public void setApiId(Long apiId) {
    this.apiId = apiId;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FilterDO other = (FilterDO) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
