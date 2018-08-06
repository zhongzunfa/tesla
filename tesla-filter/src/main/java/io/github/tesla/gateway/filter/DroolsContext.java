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
package io.github.tesla.gateway.filter;

import com.alibaba.fastjson.JSON;

import io.github.tesla.gateway.filter.springcloud.SpringCloudDiscovery;

/**
 * @author liushiming
 * @version RuleContent.java, v 0.0.1 2018年5月9日 下午8:45:37 liushiming
 */
public class DroolsContext {

  private String targetUrl;

  private String response;

  private final SpringCloudDiscovery springCloudDisCovery;

  public DroolsContext(SpringCloudDiscovery springCloudDisCovery) {
    this.springCloudDisCovery = springCloudDisCovery;
  }

  public String getTargetUrl() {
    return targetUrl;
  }

  public void setTargetUrl(String targetUrl) {
    this.targetUrl = targetUrl;
  }

  public String getResponse() {
    return response;
  }

  public void setResponse(String response) {
    this.response = response;
  }

  public String callService(String serviceId, String path, String submitJSON, String httpMethod) {
    String response = springCloudDisCovery.call(serviceId, path, httpMethod, submitJSON);
    return response;
  }

  public String toJSONString(Object obj) {
    return JSON.toJSONString(obj);
  }

  public <T> T parseObject(String json, Class<T> clazz) {
    return JSON.parseObject(json, clazz);
  }

}
