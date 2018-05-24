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

import io.github.tesla.common.domain.ApiGroupDO;

/**
 * @author liushiming
 * @version ApiGroupDao.java, v 0.0.1 2018年4月11日 下午5:49:59 liushiming
 */
@Mapper
public interface ApiGroupDao {
  ApiGroupDO get(Long id);

  ApiGroupDO load(String key);

  List<ApiGroupDO> list(Map<String, Object> map);

  int count(Map<String, Object> map);

  int save(ApiGroupDO apiGroupDO);

  int update(ApiGroupDO apiGroupDO);

  int remove(Long id);

  int batchRemove(Long[] ids);

}
