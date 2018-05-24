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
package io.github.tesla.ops.api.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import io.github.tesla.common.RequestFilterTypeEnum;
import io.github.tesla.common.ResponseFilterTypeEnum;
import io.github.tesla.common.RouteType;
import io.github.tesla.common.domain.ApiDO;
import io.github.tesla.common.domain.ApiGroupDO;
import io.github.tesla.common.domain.ApiRpcDO;
import io.github.tesla.common.domain.ApiSpringCloudDO;

/**
 * @author liushiming
 * @version ApiVo.java, v 0.0.1 2018年4月17日 下午2:17:58 liushiming
 */
public class ApiVo implements Serializable {

  private static final long serialVersionUID = 8303012923548625829L;

  private Long id;

  private String name;

  private String describe;

  private String url;

  private String httpMethod;

  private String path;

  private Integer routeType;

  private Long groupId;

  private String groupName;

  private Timestamp gmtCreate;

  private Timestamp gmtModified;

  // RPC
  private String serviceName;

  private String methodName;

  private String serviceGroup;

  private String serviceVersion;

  private byte[] protoContext;

  private String dubboParamTemplate;

  // Spring Cloud
  private String instanceId;

  // Filter
  private List<RequestFilterTypeEnum> requestFilterType;

  private List<ResponseFilterTypeEnum> responseFilterType;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescribe() {
    return describe;
  }

  public void setDescribe(String describe) {
    this.describe = describe;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public void setHttpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Long getGroupId() {
    return groupId;
  }

  public RouteType getRouteType() {
    return RouteType.fromType(Integer.valueOf(routeType));
  }

  public String getRouteTypeName() {
    return this.getRouteType().typeName();
  }

  public void setRouteType(Integer routeType) {
    this.routeType = routeType;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public Timestamp getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Timestamp gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Timestamp getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Timestamp gmtModified) {
    this.gmtModified = gmtModified;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public String getServiceGroup() {
    return serviceGroup;
  }

  public void setServiceGroup(String serviceGroup) {
    this.serviceGroup = serviceGroup;
  }

  public String getServiceVersion() {
    return serviceVersion;
  }

  public void setServiceVersion(String serviceVersion) {
    this.serviceVersion = serviceVersion;
  }

  public byte[] getProtoContext() {
    return protoContext;
  }

  public void setProtoContext(byte[] protoContext) {
    this.protoContext = protoContext;
  }

  public String getDubboParamTemplate() {
    return dubboParamTemplate;
  }

  public void setDubboParamTemplate(String dubboParamTemplate) {
    this.dubboParamTemplate = dubboParamTemplate;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public List<RequestFilterTypeEnum> getRequestFilterType() {
    return requestFilterType;
  }

  public void setRequestFilterType(List<RequestFilterTypeEnum> requestFilterType) {
    this.requestFilterType = requestFilterType;
  }

  public List<ResponseFilterTypeEnum> getResponseFilterType() {
    return responseFilterType;
  }

  public void setResponseFilterType(List<ResponseFilterTypeEnum> responseFilterType) {
    this.responseFilterType = responseFilterType;
  }


  public ApiDO buildApiDO() {
    ApiDO apiDO = new ApiDO();
    apiDO.setId(this.id);
    apiDO.setName(this.name);
    apiDO.setDescribe(this.describe);
    apiDO.setUrl(this.url);
    apiDO.setPath(this.path);
    apiDO.setRoutes(this.routeType);
    apiDO.setHttpMethod(this.httpMethod);
    ApiGroupDO apiGroup = new ApiGroupDO();
    apiGroup.setId(groupId);
    apiDO.setApiGroup(apiGroup);
    return apiDO;
  }

  public ApiRpcDO buildApiRpcDO() {
    if (this.routeType != null
        && (this.getRouteType() == RouteType.DUBBO || this.getRouteType() == RouteType.GRPC)) {
      ApiRpcDO rpcDO = new ApiRpcDO();
      rpcDO.setServiceName(this.serviceName);
      rpcDO.setMethodName(this.methodName);
      rpcDO.setServiceGroup(this.serviceGroup);
      rpcDO.setServiceVersion(this.serviceVersion);
      rpcDO.setProtoContext(this.protoContext);
      rpcDO.setDubboParamTemplate(this.dubboParamTemplate);
      return rpcDO;
    } else {
      return null;
    }
  }

  public ApiSpringCloudDO buildApiSpringCloudDO() {
    if (this.routeType != null && this.getRouteType() == RouteType.SpringCloud) {
      ApiSpringCloudDO springCloudDO = new ApiSpringCloudDO();
      springCloudDO.setInstanceId(this.instanceId);
      return springCloudDO;
    } else {
      return null;
    }
  }


  public static ApiVo buildApiVO(ApiDO apiDO, ApiRpcDO rpcDO, ApiSpringCloudDO scDO) {
    if (apiDO != null) {
      ApiVo apiVO = new ApiVo();
      apiVO.setId(apiDO.getId());
      apiVO.setName(apiDO.getName());
      apiVO.setDescribe(apiDO.getDescribe());
      apiVO.setUrl(apiDO.getUrl());
      apiVO.setHttpMethod(apiDO.getHttpMethod());
      apiVO.setPath(apiDO.getPath());
      apiVO.setGmtCreate(apiDO.getGmtCreate());
      apiVO.setGmtModified(apiDO.getGmtModified());
      apiVO.setGroupId(apiDO.getApiGroup().getId());
      apiVO.setGroupName(apiDO.getApiGroup().getName());
      apiVO.setRouteType(apiDO.getRoutes());
      // RPC
      if (apiDO.isRpc() && rpcDO != null) {
        apiVO.setServiceName(rpcDO.getServiceName());
        apiVO.setMethodName(rpcDO.getMethodName());
        apiVO.setServiceGroup(rpcDO.getServiceGroup());
        apiVO.setServiceVersion(rpcDO.getServiceVersion());
        apiVO.setProtoContext(rpcDO.getProtoContext());
        apiVO.setDubboParamTemplate(rpcDO.getDubboParamTemplate());
      }
      if (apiDO.isSpringCloud() && scDO != null) {
        // Spring Cloud
        apiVO.setInstanceId(scDO.getInstanceId());
      }
      return apiVO;
    }
    return null;
  }


}
