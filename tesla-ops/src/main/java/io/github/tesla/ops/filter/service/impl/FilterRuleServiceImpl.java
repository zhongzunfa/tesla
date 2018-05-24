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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import io.github.tesla.common.dao.FilterDao;
import io.github.tesla.common.domain.FilterDO;
import io.github.tesla.ops.api.service.ApiGroupService;
import io.github.tesla.ops.api.service.ApiService;
import io.github.tesla.ops.api.vo.ApiGroupVo;
import io.github.tesla.ops.api.vo.ApiVo;
import io.github.tesla.ops.filter.service.FilterRuleService;
import io.github.tesla.ops.filter.vo.FilterRuleVo;
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
      ruleVos.add(ruleVo);
    }
    return ruleVos;
  }

  @Override
  public int count(Map<String, Object> map) {
    return ruleDao.count(map);
  }

  @Override
  public int save(FilterRuleVo ruleVo) {
    FilterDO ruleDo = FilterRuleVo.buildFilterRuleDo(ruleVo);
    return ruleDao.save(ruleDo);
  }

  @Override
  public int update(FilterRuleVo ruleVo) {
    FilterDO ruleDo = FilterRuleVo.buildFilterRuleDo(ruleVo);
    return ruleDao.update(ruleDo);
  }

  @Override
  public int remove(Long ruleId) {
    return ruleDao.remove(ruleId);
  }

  @Override
  public int batchRemove(Long[] ruleIds) {
    return ruleDao.batchRemove(ruleIds);
  }

}
