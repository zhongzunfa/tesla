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
package io.github.tesla.gateway.netty.filter.request;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import io.github.tesla.common.RequestFilterTypeEnum;
import io.github.tesla.common.domain.FilterDO;
import io.github.tesla.common.domain.UserFilterDO;
import io.github.tesla.gateway.cache.ApiAndFilterCacheComponent;
import io.github.tesla.gateway.config.SpringContextHolder;
import io.github.tesla.gateway.filter.UserRequestFilter;
import io.github.tesla.gateway.filter.servlet.NettyHttpServletRequest;
import io.github.tesla.gateway.filter.springcloud.SpringCloudDiscovery;
import io.github.tesla.gateway.protocol.springcloud.DynamicSpringCloudClient;
import io.github.tesla.gateway.utils.ClassUtils;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;

/**
 * @author liushiming
 * @version UserDefinitionRequestFilter.java, v 0.0.1 2018年5月26日 上午1:27:01 liushiming
 */
public class UserDefinitionRequestFilter extends HttpRequestFilter {

  private final DynamicSpringCloudClient springCloudClient =
      SpringContextHolder.getBean(DynamicSpringCloudClient.class);

  @Override
  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpObject realHttpObject) {
    String uri = servletRequest.getRequestURI();
    int index = uri.indexOf("?");
    if (index > -1) {
      uri = uri.substring(0, index);
    }
    Map<String, Set<FilterDO>> rules = super.getUrlRule(UserDefinitionRequestFilter.this);
    Set<FilterDO> urlRules = rules.get(uri);
    if (urlRules == null && rules.size() > 0) {
      String path = ApiAndFilterCacheComponent.findRulePath(rules.keySet(), uri);
      if (path != null)
        urlRules = rules.get(path);
    }
    HttpResponse response = null;
    if (urlRules != null && urlRules.size() == 1) {
      FilterDO filterDo = urlRules.iterator().next();
      String classNames = filterDo.getRule();
      String[] classNameArray = StringUtils.split(classNames, UserFilterDO.DEFAULT_CLASS_SEPARATOR);
      for (String className : classNameArray) {
        Class<?> clazz = ClassUtils.getClass(className);
        if (clazz != null && UserRequestFilter.class.isAssignableFrom(clazz)
            && !Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface()) {
          UserRequestFilter userFilter = null;
          try {
            userFilter = (UserRequestFilter) clazz.newInstance();
          } catch (Throwable e) {
            e.printStackTrace();
          }
          if (userFilter != null) {
            String userRule = super.getUserRule(className, filterDo.getId());
            SpringCloudDiscovery springCloudDiscovery = springCloudClient.getSpringCloudDiscovery();
            userFilter.setSpringCloudDiscovery(springCloudDiscovery);
            userFilter.setUserRule(userRule);
            userFilter.setApplicationContext(SpringContextHolder.getApplicationContext());
            HttpResponse userResponse = userFilter.doFilter(servletRequest, realHttpObject);
            if (userResponse != null) {
              response = userResponse;
              break;
            }
          }
        }
      }
    }
    return response;
  }

  @Override
  public RequestFilterTypeEnum filterType() {
    return RequestFilterTypeEnum.UserDefinitionRequestFilter;
  }

}
