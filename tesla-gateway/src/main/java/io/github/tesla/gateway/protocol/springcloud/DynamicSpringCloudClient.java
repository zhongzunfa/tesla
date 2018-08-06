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
package io.github.tesla.gateway.protocol.springcloud;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;

import io.github.tesla.common.domain.ApiSpringCloudDO;
import io.github.tesla.gateway.config.eureka.EurekaClientConfigBean;
import io.github.tesla.gateway.config.eureka.EurekaInstanceConfigBean;
import io.github.tesla.gateway.config.eureka.InstanceInfoFactory;
import io.github.tesla.gateway.filter.springcloud.SpringCloudDiscovery;

/**
 * @author liushiming
 * @version DynamicSpringCloudClient.java, v 0.0.1 2018年5月4日 上午11:53:15 liushiming
 */
public class DynamicSpringCloudClient {

  private final EurekaInstanceConfigBean instanceConfig;

  private final EurekaClientConfigBean eurekaClientConfig;

  private DiscoveryClient eurekaClient;

  private final int httpPort;

  public DynamicSpringCloudClient(EurekaInstanceConfigBean instanceConfig,
      EurekaClientConfigBean eurekaClientConfig, int httpPort) {
    this.instanceConfig = instanceConfig;
    this.eurekaClientConfig = eurekaClientConfig;
    this.httpPort = httpPort;
  }

  private synchronized void createEurekaClient() {
    if (eurekaClient == null) {
      InstanceInfo instanceInfo = new InstanceInfoFactory().create(instanceConfig);
      ApplicationInfoManager applicationInfoManager =
          new ApplicationInfoManager(instanceConfig, instanceInfo);
      eurekaClient = new DiscoveryClient(applicationInfoManager, eurekaClientConfig);
    }
  }

  public SpringCloudDiscovery getSpringCloudDiscovery() {
    createEurekaClient();
    return new SpringCloudDiscovery(eurekaClient, httpPort);
  }



  private InstanceInfo nextServer(String serviceId) {
    this.createEurekaClient();
    try {
      return eurekaClient.getNextServerFromEureka(serviceId, false);
    } catch (Throwable e) {
      try {
        Method method = eurekaClient.getClass().getDeclaredMethod("refreshRegistry");
        method.setAccessible(true);
        method.invoke(eurekaClient, true);
      } catch (Exception e1) {
      }
      try {
        TimeUnit.SECONDS.sleep(5);
      } catch (InterruptedException e1) {
      }
      return eurekaClient.getNextServerFromEureka(serviceId, false);
    }
  }


  public String loadBalanceCall(final ApiSpringCloudDO springCloudDo) {
    String serviceId = springCloudDo.getInstanceId();
    InstanceInfo instaneinfo = this.nextServer(serviceId);
    return instaneinfo.getHostName() + ":" + instaneinfo.getPort();
  }

}
