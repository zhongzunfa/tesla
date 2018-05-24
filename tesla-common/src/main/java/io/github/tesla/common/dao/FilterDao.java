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
package io.github.tesla.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.github.tesla.common.domain.FilterDO;

/**
 * @author liushiming
 * @version FilterRuleDao.java, v 0.0.1 2018年2月11日 下午2:36:34 liushiming
 */
@Mapper
public interface FilterDao {

  FilterDO get(Long filterId);

  List<FilterDO> loadByApiId(@Param("apiId") Long apiId);

  List<FilterDO> loadByGroupId(@Param("groupId") Long groupId);

  List<FilterDO> loadCommon();

  List<FilterDO> list(Map<String, Object> map);

  int count(Map<String, Object> map);

  int save(FilterDO filter);

  int update(FilterDO filter);

  int remove(Long id);

  int batchRemove(Long[] filterIds);
}
