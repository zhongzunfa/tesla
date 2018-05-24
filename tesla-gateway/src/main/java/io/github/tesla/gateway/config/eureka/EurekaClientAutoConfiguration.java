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
package io.github.tesla.gateway.config.eureka;

import static io.github.tesla.gateway.config.eureka.util.IdUtils.getDefaultInstanceId;

import java.net.MalformedURLException;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.util.StringUtils;

import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.discovery.EurekaClientConfig;

import io.github.tesla.gateway.config.eureka.metadata.DefaultManagementMetadataProvider;
import io.github.tesla.gateway.config.eureka.metadata.ManagementMetadata;
import io.github.tesla.gateway.config.eureka.util.InetUtils;
import io.github.tesla.gateway.config.eureka.util.InetUtilsProperties;

/**
 * @author liushiming
 * @version SpringCloudConfig.java, v 0.0.1 2018年5月6日 上午10:24:11 liushiming
 */
@Configuration
@EnableConfigurationProperties
@ConditionalOnClass(EurekaClientConfig.class)
public class EurekaClientAutoConfiguration {

  @Bean
  public InetUtilsProperties inetUtilsProperties() {
    return new InetUtilsProperties();
  }

  @Bean
  @ConditionalOnMissingBean
  public InetUtils inetUtils(InetUtilsProperties properties) {
    return new InetUtils(properties);
  }

  @Bean
  @ConditionalOnMissingBean
  public DefaultManagementMetadataProvider serviceManagementMetadataProvider() {
    return new DefaultManagementMetadataProvider();
  }

  @Bean
  @ConditionalOnMissingBean(value = EurekaClientConfig.class, search = SearchStrategy.CURRENT)
  public EurekaClientConfigBean eurekaClientConfigBean(ConfigurableEnvironment env) {
    EurekaClientConfigBean client = new EurekaClientConfigBean();
    if ("bootstrap".equals(new RelaxedPropertyResolver(env).getProperty("spring.config.name"))) {
      client.setRegisterWithEureka(false);
    }
    return client;
  }

  @Bean
  @ConditionalOnMissingBean(value = EurekaInstanceConfig.class, search = SearchStrategy.CURRENT)
  public EurekaInstanceConfigBean eurekaInstanceConfigBean(InetUtils inetUtils,
      DefaultManagementMetadataProvider managementMetadataProvider, ConfigurableEnvironment env)
      throws MalformedURLException {
    PropertyResolver environmentPropertyResolver = new RelaxedPropertyResolver(env);
    PropertyResolver eurekaPropertyResolver = new RelaxedPropertyResolver(env, "eureka.instance.");
    String hostname = eurekaPropertyResolver.getProperty("hostname");
    boolean preferIpAddress =
        Boolean.parseBoolean(eurekaPropertyResolver.getProperty("preferIpAddress"));
    String ipAddress = eurekaPropertyResolver.getProperty("ipAddress");
    boolean isSecurePortEnabled =
        Boolean.parseBoolean(eurekaPropertyResolver.getProperty("securePortEnabled"));
    String serverContextPath = environmentPropertyResolver.getProperty("server.contextPath", "/");
    int serverPort = Integer.valueOf(environmentPropertyResolver.getProperty("server.port",
        environmentPropertyResolver.getProperty("port", "8080")));
    Integer managementPort =
        environmentPropertyResolver.getProperty("management.port", Integer.class);// nullable.
                                                                                  // should be
                                                                                  // wrapped into
                                                                                  // optional
    String managementContextPath =
        environmentPropertyResolver.getProperty("management.contextPath");// nullable. should be
                                                                          // wrapped into optional
    Integer jmxPort =
        environmentPropertyResolver.getProperty("com.sun.management.jmxremote.port", Integer.class);// nullable
    EurekaInstanceConfigBean instance = new EurekaInstanceConfigBean(inetUtils);
    instance.setNonSecurePort(serverPort);
    instance.setInstanceId(getDefaultInstanceId(environmentPropertyResolver));
    instance.setPreferIpAddress(preferIpAddress);
    instance.setSecurePortEnabled(isSecurePortEnabled);
    if (StringUtils.hasText(ipAddress)) {
      instance.setIpAddress(ipAddress);
    }

    if (isSecurePortEnabled) {
      instance.setSecurePort(serverPort);
    }

    if (StringUtils.hasText(hostname)) {
      instance.setHostname(hostname);
    }
    String statusPageUrlPath = eurekaPropertyResolver.getProperty("statusPageUrlPath");
    String healthCheckUrlPath = eurekaPropertyResolver.getProperty("healthCheckUrlPath");

    if (StringUtils.hasText(statusPageUrlPath)) {
      instance.setStatusPageUrlPath(statusPageUrlPath);
    }
    if (StringUtils.hasText(healthCheckUrlPath)) {
      instance.setHealthCheckUrlPath(healthCheckUrlPath);
    }

    ManagementMetadata metadata = managementMetadataProvider.get(instance, serverPort,
        serverContextPath, managementContextPath, managementPort);

    if (metadata != null) {
      instance.setStatusPageUrl(metadata.getStatusPageUrl());
      instance.setHealthCheckUrl(metadata.getHealthCheckUrl());
      if (instance.isSecurePortEnabled()) {
        instance.setSecureHealthCheckUrl(metadata.getSecureHealthCheckUrl());
      }
      Map<String, String> metadataMap = instance.getMetadataMap();
      if (metadataMap.get("management.port") == null) {
        metadataMap.put("management.port", String.valueOf(metadata.getManagementPort()));
      }
    }

    setupJmxPort(instance, jmxPort);
    return instance;
  }

  private void setupJmxPort(EurekaInstanceConfigBean instance, Integer jmxPort) {
    Map<String, String> metadataMap = instance.getMetadataMap();
    if (metadataMap.get("jmx.port") == null && jmxPort != null) {
      metadataMap.put("jmx.port", String.valueOf(jmxPort));
    }
  }
}
