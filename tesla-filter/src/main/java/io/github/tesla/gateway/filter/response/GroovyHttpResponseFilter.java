/*
 * Copyright (c) 2018 DISID CORPORATION S.L.
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

package io.github.tesla.gateway.filter.response;

import java.lang.reflect.Modifier;
import java.util.Map;
import com.google.common.collect.Maps;
import io.github.tesla.gateway.filter.UserResponseFilter;
import io.github.tesla.gateway.filter.common.GroovyCompiler;
import io.github.tesla.gateway.filter.servlet.NettyHttpServletRequest;
import io.netty.handler.codec.http.HttpResponse;

/**
 * GroovyHttpResponseFilter <br/>
 * Function: 动态执行用户自定义的Groovy脚本 <br/>
 * Date: 2018年6月7日 下午9:22:57 <br/>
 * 
 * @author liushiming
 * @version
 * @since JDK 10
 * @see
 */
public class GroovyHttpResponseFilter extends UserResponseFilter {

  private static final Map<String, UserResponseFilter> groovyInstance = Maps.newConcurrentMap();

  @Override
  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpResponse httpResponse) {
    HttpResponse response = null;
    if (userRule != null) {
      UserResponseFilter userFilter = groovyInstance.get(userRule);
      if (userFilter == null) {
        Class<?> clazz = GroovyCompiler.compile(userRule);
        if (clazz != null && UserResponseFilter.class.isAssignableFrom(clazz)
            && !Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface()) {
          try {
            userFilter = (UserResponseFilter) clazz.newInstance();
            groovyInstance.put(userRule, userFilter);
          } catch (Throwable e) {
            e.printStackTrace();
          }
        }
      }
      if (userFilter != null) {
        userFilter.setSpringCloudDiscovery(springCloudDiscovery);
        userFilter.setUserRule(userRule);
        userFilter.setApplicationContext(applicationContext);
        HttpResponse userResponse = userFilter.doFilter(servletRequest, httpResponse);
        if (userResponse != null) {
          response = userResponse;
        }
      }
    }
    return response;

  }

}

