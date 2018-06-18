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
package io.github.tesla.gateway.netty.filter.response;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import io.github.tesla.common.ResponseFilterTypeEnum;
import io.github.tesla.common.domain.FilterDO;
import io.github.tesla.common.domain.UserFilterDO;
import io.github.tesla.gateway.config.SpringContextHolder;
import io.github.tesla.gateway.filter.UserResponseFilter;
import io.github.tesla.gateway.filter.servlet.NettyHttpServletRequest;
import io.github.tesla.gateway.filter.springcloud.SpringCloudDiscovery;
import io.github.tesla.gateway.protocol.springcloud.DynamicSpringCloudClient;
import io.github.tesla.gateway.utils.ClassUtils;
import io.netty.handler.codec.http.HttpResponse;

/**
 * @author liushiming
 * @version UserDefinitionResponseFilter.java, v 0.0.1 2018年5月26日 上午1:27:29 liushiming
 */
public class UserDefinitionResponseFilter extends HttpResponseFilter {

  private final DynamicSpringCloudClient springCloudClient =
      SpringContextHolder.getBean(DynamicSpringCloudClient.class);


  @Override
  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpResponse httpResponse) {
    String uri = servletRequest.getRequestURI();
    int index = uri.indexOf("?");
    if (index > -1) {
      uri = uri.substring(0, index);
    }
    Map<String, Set<FilterDO>> rules = super.getUrlRule(UserDefinitionResponseFilter.this);
    Set<FilterDO> urlRules = rules.get(uri);
    HttpResponse response = null;
    if (urlRules != null && urlRules.size() == 1) {
      FilterDO filterDo = urlRules.iterator().next();
      String classNames = filterDo.getRule();
      String[] classNameArray = StringUtils.split(classNames, UserFilterDO.DEFAULT_CLASS_SEPARATOR);
      for (String className : classNameArray) {
        Class<?> clazz = ClassUtils.getClass(className);
        if (clazz != null && UserResponseFilter.class.isAssignableFrom(clazz)
            && !Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface()) {
          // 如果userFilter不能实例化，则调用下一个Filter
          UserResponseFilter userFilter = null;
          try {
            userFilter = (UserResponseFilter) clazz.newInstance();
          } catch (Throwable e) {
            e.printStackTrace();
          }
          if (userFilter != null) {
            String userRule = super.getUserRule(className, filterDo.getId());
            SpringCloudDiscovery springCloudDiscovery = springCloudClient.getSpringCloudDiscovery();
            userFilter.setSpringCloudDiscovery(springCloudDiscovery);
            userFilter.setUserRule(userRule);
            HttpResponse userResponse = userFilter.doFilter(servletRequest, httpResponse);
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
  public ResponseFilterTypeEnum filterType() {
    return ResponseFilterTypeEnum.UserDefinitionResponseFilter;
  }

}
