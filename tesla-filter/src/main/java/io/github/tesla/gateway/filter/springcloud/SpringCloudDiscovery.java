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
package io.github.tesla.gateway.filter.springcloud;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;


/**
 * @author liushiming
 * @version UserFilter.java, v 0.0.1 2018年5月26日 上午12:33:50 liushiming
 */
public class SpringCloudDiscovery {

  private static Logger LOG = LoggerFactory.getLogger(SpringCloudDiscovery.class);

  private final OkHttpClient okHttpClient = new OkHttpClient();

  private final DiscoveryClient eurekaClient;

  private final int gateWayPort;


  public SpringCloudDiscovery(DiscoveryClient eurekaClient, int gateWayPort) {
    super();
    this.eurekaClient = eurekaClient;
    this.gateWayPort = gateWayPort;
  }

  public InstanceInfo nextServer(String serviceId) {
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

  public String call(String submitServiceId, String submitUrl, String submitType,
      String submitJSON) {
    final String httpUrl;
    if (submitServiceId != null) {
      InstanceInfo serviceInstance = this.nextServer(submitServiceId);
      httpUrl = buildUrl(submitUrl, serviceInstance.getHostName(), serviceInstance.getPort());
    } else if (StringUtils.startsWith(submitUrl, "http")
        || StringUtils.startsWith(submitUrl, "https")) {
      httpUrl = submitUrl;
    } else {
      httpUrl = buildUrl(submitUrl, "localhost", gateWayPort);
    }
    final Response response;
    final Request request;
    try {
      if ("POST".equalsIgnoreCase(submitType) && submitJSON != null) {
        MediaType medialType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(medialType, submitJSON);
        request = new Request.Builder()//
            .url(httpUrl)//
            .post(requestBody)//
            .build();
        response = okHttpClient.newCall(request).execute();
      } else {
        request = new Request.Builder()//
            .url(httpUrl)//
            .get()//
            .build();
        response = okHttpClient.newCall(request).execute();
      }
      return response.isSuccessful() ? response.body().string() : null;
    } catch (Throwable e) {
      LOG.error("call Remote service error,url is:" + httpUrl + ",body is:" + submitJSON, e);
    }
    return null;
  }

  private static Pattern HTTP_PREFIX = Pattern.compile("^https?://.*", Pattern.CASE_INSENSITIVE);

  private String buildUrl(String path, String httpHost, int port) {
    final String url;
    if (HTTP_PREFIX.matcher(path).matches()) {
      url = path;
    } else {
      if (!path.startsWith("/")) {
        path = "/" + path;
      }
      url = String.format("http://%s:%s%s", httpHost, port, path);
    }
    return url;
  }


}
