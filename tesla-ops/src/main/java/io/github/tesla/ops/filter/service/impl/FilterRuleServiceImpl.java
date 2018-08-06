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
package io.github.tesla.ops.filter.service.impl;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.Lists;
import io.github.tesla.common.RequestFilterTypeEnum;
import io.github.tesla.common.ResponseFilterTypeEnum;
import io.github.tesla.common.dao.FilterDao;
import io.github.tesla.common.dao.UserFilterDao;
import io.github.tesla.common.domain.FilterDO;
import io.github.tesla.common.domain.UserFilterDO;
import io.github.tesla.ops.api.service.ApiGroupService;
import io.github.tesla.ops.api.service.ApiService;
import io.github.tesla.ops.api.vo.ApiGroupVo;
import io.github.tesla.ops.api.vo.ApiVo;
import io.github.tesla.ops.common.CommonResponse;
import io.github.tesla.ops.filter.service.FilterRuleService;
import io.github.tesla.ops.filter.vo.FilterRuleVo;
import io.github.tesla.ops.filter.vo.UserFilterRuleVo;
import io.github.tesla.ops.system.domain.PageDO;
import io.github.tesla.ops.utils.Query;

/**
 * @author liushiming
 * @version FilterRuleServiceImpl.java, v 0.0.1 2018年3月20日 上午11:02:55 liushiming
 */
@Service
public class FilterRuleServiceImpl implements FilterRuleService {

  @Autowired
  private FilterDao ruleDao;

  @Autowired
  private UserFilterDao userFilterDao;

  @Autowired
  private ApiGroupService groupService;

  @Autowired
  private ApiService apiService;

  @Override
  public PageDO<FilterRuleVo> queryList(Query query) {
    int total = ruleDao.count(query);
    List<FilterDO> ruleDos = ruleDao.list(query);
    List<FilterRuleVo> ruleVos = Lists.newArrayListWithCapacity(ruleDos.size());
    for (FilterDO ruleDo : ruleDos) {
      Long apiId = ruleDo.getApiId();
      Long groupId = ruleDo.getGroupId();
      ApiVo apiVo = null;
      ApiGroupVo groupVo = null;
      if (apiId != null) {
        apiVo = apiService.get(apiId);
      }
      if (groupId != null) {
        groupVo = groupService.get(groupId);
      }
      FilterRuleVo ruleVo = FilterRuleVo.buildFilterRuleVo(ruleDo, apiVo, groupVo);
      this.loadUserFilter(ruleVo);
      ruleVos.add(ruleVo);
    }
    PageDO<FilterRuleVo> page = new PageDO<>();
    page.setTotal(total);
    page.setRows(ruleVos);
    return page;
  }

  @Override
  public FilterRuleVo get(Long ruleId) {
    FilterDO ruleDo = ruleDao.get(ruleId);
    Long apiId = ruleDo.getApiId();
    Long groupId = ruleDo.getGroupId();
    ApiVo apiVo = null;
    ApiGroupVo groupVo = null;
    if (apiId != null) {
      apiVo = apiService.get(apiId);
    }
    if (groupId != null) {
      groupVo = groupService.get(groupId);
    }
    FilterRuleVo ruleVo = FilterRuleVo.buildFilterRuleVo(ruleDo, apiVo, groupVo);
    this.loadUserFilter(ruleVo);
    return ruleVo;
  }

  @Override
  public List<FilterRuleVo> list(Map<String, Object> map) {
    List<FilterDO> ruleDos = ruleDao.list(map);
    List<FilterRuleVo> ruleVos = Lists.newArrayListWithCapacity(ruleDos.size());
    for (FilterDO ruleDo : ruleDos) {
      Long apiId = ruleDo.getApiId();
      Long groupId = ruleDo.getGroupId();
      ApiVo apiVo = null;
      ApiGroupVo groupVo = null;
      if (apiId != null) {
        apiVo = apiService.get(apiId);
      }
      if (groupId != null) {
        groupVo = groupService.get(groupId);
      }
      FilterRuleVo ruleVo = FilterRuleVo.buildFilterRuleVo(ruleDo, apiVo, groupVo);
      this.loadUserFilter(ruleVo);
      ruleVos.add(ruleVo);
    }
    return ruleVos;
  }

  @Override
  public int count(Map<String, Object> map) {
    return ruleDao.count(map);
  }

  @Override
  @Transactional
  public int save(FilterRuleVo ruleVo) {
    FilterDO ruleDo = FilterRuleVo.buildFilterRuleDo(ruleVo);
    ruleDao.save(ruleDo);
    List<UserFilterRuleVo> userFilters = ruleVo.getUserFilter();
    if (userFilters != null) {
      for (UserFilterRuleVo userFilterVo : userFilters) {
        UserFilterDO userFilterDO = UserFilterRuleVo.buildUserFilterRuleDo(userFilterVo);
        userFilterDO.setFilterId(ruleDo.getId());
        userFilterDao.save(userFilterDO);
      }
    }
    return CommonResponse.SUCCESS;
  }

  @Override
  @Transactional
  public int update(FilterRuleVo ruleVo) {
    FilterDO ruleDo = FilterRuleVo.buildFilterRuleDo(ruleVo);
    ruleDao.update(ruleDo);
    List<UserFilterRuleVo> userFilters = ruleVo.getUserFilter();
    if (userFilters != null) {
      userFilterDao.removeByFilterId(ruleDo.getId());
      for (UserFilterRuleVo userFilterVo : userFilters) {
        UserFilterDO userFilterDO = UserFilterRuleVo.buildUserFilterRuleDo(userFilterVo);
        userFilterDO.setFilterId(ruleDo.getId());
        userFilterDao.save(userFilterDO);
      }
    }
    return CommonResponse.SUCCESS;
  }

  @Override
  @Transactional
  public int remove(Long ruleId) {
    userFilterDao.removeByFilterId(ruleId);
    return ruleDao.remove(ruleId);
  }

  @Override
  @Transactional
  public int batchRemove(Long[] ruleIds) {
    for (Long ruleId : ruleIds) {
      userFilterDao.removeByFilterId(ruleId);
    }
    return ruleDao.batchRemove(ruleIds);
  }

  @Override
  @Transactional
  public int removeByApiId(Long apiId) {
    List<FilterDO> filterList = ruleDao.loadByApiId(apiId);
    List<Long> filterIds = Lists.newArrayListWithCapacity(filterList.size());
    Long[] ruleIds = new Long[filterList.size()];
    for (FilterDO filter : filterList) {
      filterIds.add(filter.getId());
    }
    filterIds.toArray(ruleIds);
    return batchRemove(ruleIds);
  }

  @Override
  @Transactional
  public int removeByGroupId(Long groupId) {
    List<FilterDO> filterList = ruleDao.loadByGroupId(groupId);
    List<Long> filterIds = Lists.newArrayListWithCapacity(filterList.size());
    Long[] ruleIds = new Long[filterList.size()];
    for (FilterDO filter : filterList) {
      filterIds.add(filter.getId());
    }
    filterIds.toArray(ruleIds);
    return batchRemove(ruleIds);
  }

  private void loadUserFilter(FilterRuleVo filterRuleVo) {
    RequestFilterTypeEnum userDefinitionRequestFilter =
        RequestFilterTypeEnum.UserDefinitionRequestFilter;
    ResponseFilterTypeEnum userDefinitionResponseFilter =
        ResponseFilterTypeEnum.UserDefinitionResponseFilter;
    String filterType = filterRuleVo.getFilterType();
    Long filterId = filterRuleVo.getId();
    if (filterType.equals(userDefinitionRequestFilter.name())
        || filterType.equals(userDefinitionResponseFilter.name())) {
      String rule = filterRuleVo.getRule();
      String[] classNames = StringUtils.split(rule, UserFilterDO.DEFAULT_CLASS_SEPARATOR);
      List<UserFilterRuleVo> userFilters = Lists.newArrayList();
      for (String className : classNames) {
        UserFilterDO userFilterDo =
            this.userFilterDao.loadByFilterIdAndClassName(filterId, className);
        if (userFilterDo != null) {
          UserFilterRuleVo userFilter = UserFilterRuleVo.buildUserFilterRuleVo(userFilterDo);
          userFilters.add(userFilter);
        }
      }
      filterRuleVo.setUserFilter(userFilters);
    }

  }



}
