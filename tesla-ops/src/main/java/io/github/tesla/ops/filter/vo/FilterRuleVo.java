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
import java.util.List;

import org.springframework.beans.BeanUtils;

import io.github.tesla.common.RequestFilterTypeEnum;
import io.github.tesla.common.ResponseFilterTypeEnum;
import io.github.tesla.common.domain.FilterDO;
import io.github.tesla.ops.api.vo.ApiGroupVo;
import io.github.tesla.ops.api.vo.ApiVo;

/**
 * @author liushiming
 * @version FilterRuleVo.java, v 0.0.1 2018年5月14日 上午11:01:02 liushiming
 */
public class FilterRuleVo implements Serializable {
  private static final long serialVersionUID = -6539950412372279036L;

  private Long id;

  private String name;

  private String describe;

  private String inOrOut;

  private String filterType;

  private String rule;

  private ApiVo api;

  private ApiGroupVo group;

  private List<UserFilterRuleVo> userFilter;

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


  public String getInOrOut() {
    return inOrOut;
  }

  public void setInOrOut(String inOrOut) {
    this.inOrOut = inOrOut;
  }


  public String getFilterType() {
    return filterType;
  }

  public void setFilterType(String filterType) {
    this.filterType = filterType;
  }


  public String getFilterName() {
    if ("IN".equals(inOrOut)) {
      RequestFilterTypeEnum requestType = RequestFilterTypeEnum.fromTypeName(filterType);
      if (requestType != null) {
        return requestType.filterViewName();
      } else {
        return null;
      }
    } else if ("OUT".equals(inOrOut)) {
      ResponseFilterTypeEnum responseType = ResponseFilterTypeEnum.fromTypeName(filterType);
      if (responseType != null) {
        return responseType.filterViewName();
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  public Integer getFilterOrder() {
    if ("IN".equals(inOrOut)) {
      RequestFilterTypeEnum requestType = RequestFilterTypeEnum.fromTypeName(filterType);
      if (requestType != null) {
        return requestType.order();
      } else {
        return null;
      }
    } else if ("OUT".equals(inOrOut)) {
      ResponseFilterTypeEnum responseType = ResponseFilterTypeEnum.fromTypeName(filterType);
      if (responseType != null) {
        return responseType.order();
      } else {
        return null;
      }
    } else {
      return null;
    }
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRule() {
    return rule;
  }

  public void setRule(String rule) {
    this.rule = rule;
  }

  public ApiVo getApi() {
    return api;
  }

  public void setApi(ApiVo api) {
    this.api = api;
  }

  public ApiGroupVo getGroup() {
    return group;
  }

  public void setGroup(ApiGroupVo group) {
    this.group = group;
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


  public List<UserFilterRuleVo> getUserFilter() {
    return userFilter;
  }

  public void setUserFilter(List<UserFilterRuleVo> userFilter) {
    this.userFilter = userFilter;
  }

  public static FilterRuleVo buildFilterRuleVo(FilterDO filterDo, ApiVo apiVo, ApiGroupVo groupVo) {
    FilterRuleVo ruleVo = new FilterRuleVo();
    BeanUtils.copyProperties(filterDo, ruleVo);
    if (apiVo != null)
      ruleVo.setApi(apiVo);
    if (groupVo != null)
      ruleVo.setGroup(groupVo);
    return ruleVo;
  }

  public static FilterDO buildFilterRuleDo(FilterRuleVo filterVo) {
    FilterDO ruleDO = new FilterDO();
    BeanUtils.copyProperties(filterVo, ruleDO);
    if (filterVo.getApi() != null)
      ruleDO.setApiId(filterVo.getApi().getId());
    if (filterVo.getGroup() != null)
      ruleDO.setGroupId(filterVo.getGroup().getId());
    return ruleDO;
  }

}
