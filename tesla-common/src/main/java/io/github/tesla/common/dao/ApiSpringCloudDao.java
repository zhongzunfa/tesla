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

import io.github.tesla.common.domain.ApiSpringCloudDO;

/**
 * @author liushiming
 * @version ApiSpringCloudDao.java, v 0.0.1 2018年4月11日 下午5:48:37 liushiming
 */
@Mapper
public interface ApiSpringCloudDao {
  ApiSpringCloudDO get(@Param("apiId") Long apiId);

  List<ApiSpringCloudDO> list(Map<String, Object> map);

  int save(ApiSpringCloudDO springCloud);

  int update(ApiSpringCloudDO springCloud);

  int remove(@Param("apiId") Long apiId);

  int batchRemove(Long[] apiIds);
}
