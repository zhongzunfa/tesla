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
package io.github.tesla.ops.filter.vo;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.beans.BeanUtils;

import io.github.tesla.common.domain.UserFilterDO;

public class UserFilterRuleVo implements Serializable {
  private static final long serialVersionUID = 6827905552520953823L;

  private Long id;

  private String filterClass;

  private String rule;

  private Long filterId;

  private Timestamp gmtCreate;

  private Timestamp gmtModified;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFilterClass() {
    return filterClass;
  }

  public void setFilterClass(String filterClass) {
    this.filterClass = filterClass;
  }

  public String getRule() {
    return rule;
  }

  public void setRule(String rule) {
    this.rule = rule;
  }

  public Long getFilterId() {
    return filterId;
  }

  public void setFilterId(Long filterId) {
    this.filterId = filterId;
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

  public static UserFilterRuleVo buildUserFilterRuleVo(UserFilterDO userFilterDo) {
    UserFilterRuleVo ruleVo = new UserFilterRuleVo();
    BeanUtils.copyProperties(userFilterDo, ruleVo);
    return ruleVo;
  }

  public static UserFilterDO buildUserFilterRuleDo(UserFilterRuleVo userFilterVo) {
    UserFilterDO ruleDO = new UserFilterDO();
    BeanUtils.copyProperties(userFilterVo, ruleDO);
    return ruleDO;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((filterClass == null) ? 0 : filterClass.hashCode());
    result = prime * result + ((filterId == null) ? 0 : filterId.hashCode());
    result = prime * result + ((gmtCreate == null) ? 0 : gmtCreate.hashCode());
    result = prime * result + ((gmtModified == null) ? 0 : gmtModified.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((rule == null) ? 0 : rule.hashCode());
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
    UserFilterRuleVo other = (UserFilterRuleVo) obj;
    if (filterClass == null) {
      if (other.filterClass != null)
        return false;
    } else if (!filterClass.equals(other.filterClass))
      return false;
    if (filterId == null) {
      if (other.filterId != null)
        return false;
    } else if (!filterId.equals(other.filterId))
      return false;
    if (gmtCreate == null) {
      if (other.gmtCreate != null)
        return false;
    } else if (!gmtCreate.equals(other.gmtCreate))
      return false;
    if (gmtModified == null) {
      if (other.gmtModified != null)
        return false;
    } else if (!gmtModified.equals(other.gmtModified))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (rule == null) {
      if (other.rule != null)
        return false;
    } else if (!rule.equals(other.rule))
      return false;
    return true;
  }


}
