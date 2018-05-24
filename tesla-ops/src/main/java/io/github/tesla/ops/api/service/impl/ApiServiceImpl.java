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
package io.github.tesla.ops.api.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import io.github.tesla.common.dao.ApiDao;
import io.github.tesla.common.dao.ApiRpcDao;
import io.github.tesla.common.dao.ApiSpringCloudDao;
import io.github.tesla.common.domain.ApiDO;
import io.github.tesla.common.domain.ApiRpcDO;
import io.github.tesla.common.domain.ApiSpringCloudDO;
import io.github.tesla.ops.api.service.ApiService;
import io.github.tesla.ops.api.vo.ApiVo;
import io.github.tesla.ops.common.CommonResponse;
import io.github.tesla.ops.system.domain.PageDO;
import io.github.tesla.ops.utils.Query;

/**
 * @author liushiming
 * @version routeServiceImpl.java, v 0.0.1 2018年1月8日 上午11:38:49 liushiming
 */
@Service
public class ApiServiceImpl implements ApiService {

  @Autowired
  private ApiDao apiDao;

  @Autowired
  private ApiRpcDao rpcDao;

  @Autowired
  private ApiSpringCloudDao springCloudDao;

  @Override
  public PageDO<ApiVo> queryList(Query query) {
    int total = apiDao.count(query);
    List<ApiDO> apiDOs = apiDao.list(query);
    List<ApiVo> apiVOs = Lists.newArrayListWithCapacity(apiDOs.size());
    for (ApiDO apiDO : apiDOs) {
      Long apiId = apiDO.getId();
      Boolean isRpc = apiDO.isRpc();
      Boolean isSpringCloud = apiDO.isSpringCloud();
      ApiRpcDO rpcDO = null;
      ApiSpringCloudDO springCloudDO = null;
      if (isRpc) {
        rpcDO = rpcDao.get(apiId);
      }
      if (isSpringCloud) {
        springCloudDO = springCloudDao.get(apiId);
      }
      ApiVo apiVO = ApiVo.buildApiVO(apiDO, rpcDO, springCloudDO);
      apiVOs.add(apiVO);
    }
    PageDO<ApiVo> page = new PageDO<>();
    page.setTotal(total);
    page.setRows(apiVOs);
    return page;
  }

  @Override
  public ApiVo get(Long id) {
    ApiDO apiDO = apiDao.get(id);
    Long apiId = apiDO.getId();
    Boolean isRpc = apiDO.isRpc();
    Boolean isSpringCloud = apiDO.isSpringCloud();
    ApiRpcDO rpcDO = null;
    ApiSpringCloudDO springCloudDO = null;
    if (isRpc) {
      rpcDO = rpcDao.get(apiId);
    }
    if (isSpringCloud) {
      springCloudDO = springCloudDao.get(apiId);
    }
    ApiVo apiVO = ApiVo.buildApiVO(apiDO, rpcDO, springCloudDO);
    return apiVO;
  }

  @Override
  public List<ApiVo> list(Map<String, Object> map) {
    List<ApiDO> apiDOs = apiDao.list(map);
    List<ApiVo> apiVOs = Lists.newArrayListWithCapacity(apiDOs.size());
    for (ApiDO apiDO : apiDOs) {
      Long apiId = apiDO.getId();
      Boolean isRpc = apiDO.isRpc();
      Boolean isSpringCloud = apiDO.isSpringCloud();
      ApiRpcDO rpcDO = null;
      ApiSpringCloudDO springCloudDO = null;
      if (isRpc) {
        rpcDO = rpcDao.get(apiId);
      }
      if (isSpringCloud) {
        springCloudDO = springCloudDao.get(apiId);
      }
      ApiVo apiVO = ApiVo.buildApiVO(apiDO, rpcDO, springCloudDO);
      apiVOs.add(apiVO);
    }
    return apiVOs;
  }

  @Override
  public int count(Map<String, Object> map) {
    int total = apiDao.count(map);
    return total;
  }

  @Override
  @Transactional
  public int save(ApiVo vo) {
    ApiDO apiDO = vo.buildApiDO();
    apiDao.save(apiDO);
    ApiRpcDO rpcDO = vo.buildApiRpcDO();
    ApiSpringCloudDO springCloudDO = vo.buildApiSpringCloudDO();
    if (rpcDO != null) {
      rpcDO.setApi(apiDO);
      rpcDao.save(rpcDO);
    }
    if (springCloudDO != null) {
      springCloudDO.setApi(apiDO);
      springCloudDao.save(springCloudDO);
    }
    return CommonResponse.SUCCESS;
  }

  @Override
  @Transactional
  public int update(ApiVo vo) {
    ApiDO apiDO = vo.buildApiDO();
    apiDao.update(apiDO);
    ApiRpcDO rpcDO = vo.buildApiRpcDO();
    ApiSpringCloudDO springCloudDO = vo.buildApiSpringCloudDO();
    if (rpcDO != null) {
      rpcDO.setApi(apiDO);
      rpcDao.update(rpcDO);
    }
    if (springCloudDO != null) {
      springCloudDO.setApi(apiDO);
      springCloudDao.update(springCloudDO);
    }
    return CommonResponse.SUCCESS;
  }

  @Override
  @Transactional
  public int remove(Long id) {
    rpcDao.remove(id);
    springCloudDao.remove(id);
    apiDao.remove(id);
    return CommonResponse.SUCCESS;
  }

  @Override
  @Transactional
  public int batchRemove(Long[] ids) {
    rpcDao.batchRemove(ids);
    springCloudDao.batchRemove(ids);
    apiDao.batchRemove(ids);
    return CommonResponse.SUCCESS;
  }
}
