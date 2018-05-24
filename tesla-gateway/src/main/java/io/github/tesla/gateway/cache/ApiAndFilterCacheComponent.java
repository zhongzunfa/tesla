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
package io.github.tesla.gateway.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import io.github.tesla.common.dao.ApiDao;
import io.github.tesla.common.dao.ApiRpcDao;
import io.github.tesla.common.dao.ApiSpringCloudDao;
import io.github.tesla.common.dao.FilterDao;
import io.github.tesla.common.domain.ApiDO;
import io.github.tesla.common.domain.ApiGroupDO;
import io.github.tesla.common.domain.ApiRpcDO;
import io.github.tesla.common.domain.ApiSpringCloudDO;
import io.github.tesla.common.domain.FilterDO;
import io.github.tesla.gateway.netty.filter.AbstractCommonFilter;

/**
 * @author liushiming
 * @version RouteCacheComponent.java, v 0.0.1 2018年1月26日 上午11:25:08 liushiming
 */
@Component
public class ApiAndFilterCacheComponent extends AbstractScheduleCache {

  private static final PathMatcher pathMatcher = new AntPathMatcher();

  // 直接路由
  private static final Map<String, Pair<String, String>> REDIRECT_ROUTE = Maps.newConcurrentMap();

  // RPC服务发现
  private static final Map<String, ApiRpcDO> RPC_ROUTE = Maps.newConcurrentMap();

  // SpringCloud服务发现
  private static final Map<String, Pair<String, ApiSpringCloudDO>> SPRINGCLOUD_ROUTE =
      Maps.newConcurrentMap();

  // 针对所有url的过滤规则,Key是Filter类型
  private static final Map<String, Set<String>> COMMUNITY_RULE_CACHE = Maps.newConcurrentMap();

  // 针对特定url的过滤规则，外部的Key是Filter类型，内部的key是url
  private static final Map<String, Map<String, Set<String>>> URL_RULE_CACHE =
      Maps.newConcurrentMap();

  private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

  @Autowired
  private ApiDao apiDao;

  @Autowired
  private ApiRpcDao rpcDao;

  @Autowired
  private ApiSpringCloudDao springCloudDao;

  @Autowired
  private FilterDao filterDao;


  @Override
  protected void doCache() {
    try {
      readWriteLock.writeLock().lock();
      clearCache();
      List<ApiDO> apis = apiDao.list(Maps.newHashMap());
      for (ApiDO api : apis) {
        ApiDO apiClone = api.copy();
        String url = apiClone.getUrl();
        Long apiId = apiClone.getId();
        ApiGroupDO group = apiClone.getApiGroup();
        this.doCacheRoute(apiClone, url, apiId, group);
        this.doCacheFilter(apiClone, url, apiId, group.getId());
      }
      List<FilterDO> filterDOs = filterDao.loadCommon();
      for (FilterDO filterDO : filterDOs) {
        String type = filterDO.getFilterType().name();
        String rule = filterDO.getRule();
        Set<String> rules = COMMUNITY_RULE_CACHE.get(type);
        if (rules == null) {
          rules = Sets.newHashSet();
          COMMUNITY_RULE_CACHE.put(type, rules);
        }
        rules.add(rule);
      }

    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  private void clearCache() {
    REDIRECT_ROUTE.clear();
    RPC_ROUTE.clear();
    SPRINGCLOUD_ROUTE.clear();
    COMMUNITY_RULE_CACHE.clear();
    URL_RULE_CACHE.clear();
  }

  private void doCacheFilter(ApiDO apiClone, String url, Long apiId, Long groupId) {
    List<FilterDO> filterDO1 = filterDao.loadByApiId(apiId);
    List<FilterDO> filterDO2 = filterDao.loadByGroupId(groupId);
    Set<FilterDO> filterDOs = Sets.newHashSet();
    filterDOs.addAll(filterDO1);
    filterDOs.addAll(filterDO2);
    for (FilterDO filterDO : filterDOs) {
      String type = filterDO.getFilterType().name();
      String rule = filterDO.getRule();
      Map<String, Set<String>> maprules = URL_RULE_CACHE.get(type);
      if (maprules == null) {
        maprules = Maps.newConcurrentMap();
        URL_RULE_CACHE.put(type, maprules);
      }
      Set<String> rules = maprules.get(url);
      if (rules == null) {
        rules = Sets.newHashSet();
        maprules.put(url, rules);
      }
      rules.add(rule);
    }
  }

  private void doCacheRoute(ApiDO apiClone, String url, Long apiId, ApiGroupDO group) {
    // RPC路由
    if (apiClone.isRpc()) {
      ApiRpcDO rpc = rpcDao.get(apiId);
      RPC_ROUTE.put(url, rpc);
    } // SpringCloud路由
    else if (apiClone.isSpringCloud()) {
      final String backEndPath = group.getBackendPath();
      final String urlPath;
      if (StringUtils.isNoneBlank(backEndPath)) {
        urlPath = path(backEndPath) + path(apiClone.getPath());
      } else {
        urlPath = apiClone.getPath();
      }
      ApiSpringCloudDO springCloud = springCloudDao.get(apiId);
      SPRINGCLOUD_ROUTE.put(url, new MutablePair<String, ApiSpringCloudDO>(urlPath, springCloud));
    } // 直接路由
    else {
      String backEndHost = group.getBackendHost();
      String backEndPort = group.getBackendPort();
      final String backEndPath = group.getBackendPath();
      final String urlPath;
      if (StringUtils.isNotBlank(backEndPath)) {
        urlPath = path(backEndPath) + path(apiClone.getPath());
      } else {
        urlPath = apiClone.getPath();
      }
      REDIRECT_ROUTE.put(url,
          new MutablePair<String, String>(backEndHost + ":" + backEndPort, urlPath));
    }
  }

  private String path(String path) {
    if (path.startsWith("/")) {
      return path;
    } else {
      return "/" + path;
    }
  }


  public Pair<String, String> getDirectRoute(String actorPath) {
    try {
      readWriteLock.readLock().lock();
      Set<String> allRoutePath = REDIRECT_ROUTE.keySet();
      for (String path : allRoutePath) {
        if (path.equals(actorPath) || pathMatcher.match(path, actorPath)) {
          try {
            return REDIRECT_ROUTE.get(path);
          } catch (Throwable e) {
            return null;
          }
        }
      }
      return null;
    } finally {
      readWriteLock.readLock().unlock();
    }

  }

  public ApiRpcDO getRpcRoute(String actorPath) {
    try {
      readWriteLock.readLock().lock();
      return RPC_ROUTE.get(actorPath);
    } finally {
      readWriteLock.readLock().unlock();
    }
  }

  public Pair<String, ApiSpringCloudDO> getSpringCloudRoute(String actorPath) {
    try {
      readWriteLock.readLock().lock();
      return SPRINGCLOUD_ROUTE.get(actorPath);
    } finally {
      readWriteLock.readLock().unlock();
    }
  }

  public Set<String> getPubicFilterRule(AbstractCommonFilter filter) {
    try {
      readWriteLock.readLock().lock();
      String type = filter.filterName();
      Set<String> rules = COMMUNITY_RULE_CACHE.get(type);
      if (rules == null) {
        rules = Sets.newHashSet();
      }
      return rules;
    } finally {
      readWriteLock.readLock().unlock();
    }

  }


  public Map<String, Set<String>> getUrlFilterRule(AbstractCommonFilter filter) {
    try {
      readWriteLock.readLock().lock();
      String type = filter.filterName();
      Map<String, Set<String>> patterns = URL_RULE_CACHE.get(type);
      if (patterns == null) {
        patterns = Maps.newConcurrentMap();
      }
      return patterns;
    } finally {
      readWriteLock.readLock().unlock();
    }

  }


}
