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
package io.github.tesla.gateway;

import java.io.IOException;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;

import io.github.tesla.gateway.netty.HttpFiltersSourceAdapter;
import io.github.tesla.gateway.netty.HttpProxyServer;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;

/**
 * @author liushiming
 * @version TeslaGateWayApplication.java, v 0.0.1 2018年1月24日 下午4:37:37 liushiming
 */
@MapperScan(basePackages = {"io.github.tesla.common"}, annotationClass = Mapper.class)
@SpringBootApplication(
    exclude = {ValidationAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class TeslaGateWayApplication implements CommandLineRunner {

  @Value("${server.port}")
  private int httpPort;


  public static void main(String[] args) {
    SpringApplication.run(TeslaGateWayApplication.class, args);
  }

  @Override
  public void run(String... arg0) throws Exception {
    runPrometheusServer();
    runNettyServer();
  }

  private void runNettyServer() {
    HttpProxyServer.bootstrap()//
        .withPort(httpPort)//
        .withFiltersSource(new HttpFiltersSourceAdapter())//
        .withAllowRequestToOriginServer(true)//
        .withAllowLocalOnly(false)//
        .start();
  }

  private void runPrometheusServer() throws IOException {
    final int metricePort = httpPort + 1;
    DefaultExports.initialize();
    try {
      new HTTPServer(metricePort, true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
