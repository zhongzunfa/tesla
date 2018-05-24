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
package io.github.tesla.gateway.protocol.dubbo;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.JSONOutputFormat;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import io.github.tesla.common.domain.ApiRpcDO;
import io.github.tesla.gateway.netty.filter.help.BodyMapping;
import io.github.tesla.gateway.netty.filter.help.HeaderMapping;
import io.github.tesla.gateway.netty.servlet.NettyHttpServletRequest;
import io.github.tesla.gateway.protocol.MicroserviceDynamicClient;

/**
 * @author liushiming
 * @version DynamicDubboClient.java, v 0.0.1 2018年1月29日 下午2:38:28 liushiming
 */
public class DynamicDubboClient extends MicroserviceDynamicClient {

  private final ApplicationConfig applicationConfig;

  private final RegistryConfig registryConfig;

  private final StringTemplateLoader templateHolder = new StringTemplateLoader();

  private final Configuration configuration;

  public DynamicDubboClient(final ApplicationConfig applicationConfig,
      RegistryConfig registryConfig) {
    super();
    this.applicationConfig = applicationConfig;
    this.registryConfig = registryConfig;
    Configuration configuration_ = new Configuration(Configuration.VERSION_2_3_26);
    configuration_.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_26));
    configuration_.setOutputFormat(JSONOutputFormat.INSTANCE);
    configuration_.setTemplateLoader(templateHolder);
    DefaultObjectWrapperBuilder owb = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_26);
    owb.setIterableSupport(true);
    configuration_.setObjectWrapper(owb.build());
    this.configuration = configuration_;
  }

  private String cacheTemplate(final ApiRpcDO rpcDo) {
    final String templateContent = this.buildFreemarkerTemplate(rpcDo.getDubboParamTemplate());
    final String templateKey = rpcDo.getServiceName() + "_" + rpcDo.getMethodName();
    templateHolder.putTemplate(templateKey, templateContent);
    return templateKey;
  }

  private String buildFreemarkerTemplate(final String templateContent) {
    Object templateJson = JSON.parse(templateContent);
    StringBuilder sb = new StringBuilder();
    sb.append("<#assign jsonObj = input.path(\"$\")>");
    sb.append("<#assign jsonStr = input.json(\"$\")>");
    sb.append("{");
    if (templateJson instanceof JSONArray) {
      for (Iterator<Object> it = ((JSONArray) templateJson).iterator(); it.hasNext();) {
        JSONObject jsonObj = (JSONObject) it.next();
        sb.append("\"" + jsonObj.getString("type") + "\"");
        sb.append(":");
        sb.append("\"" + jsonObj.getString("expression") + "\"");
        if (it.hasNext()) {
          sb.append(",");;
        }
      }
    } else if (templateJson instanceof JSONObject) {
      JSONObject jsonObj = (JSONObject) templateJson;
      sb.append(jsonObj.getString("type"));
      sb.append(":");
      sb.append("\"" + jsonObj.getString("expression") + "\"");
    }
    sb.append("}");
    return sb.toString();
  }

  private String doDataMapping(final String templateKey,
      final NettyHttpServletRequest servletRequest) throws TemplateNotFoundException,
      MalformedTemplateNameException, ParseException, IOException, TemplateException {
    Map<String, Object> templateContext = new HashMap<String, Object>();
    BodyMapping body = new BodyMapping(servletRequest);
    body.shouldReplace();
    templateContext.put("header", new HeaderMapping(servletRequest));
    templateContext.put("input", body);
    Template template = configuration.getTemplate(templateKey);
    StringWriter outPutWrite = new StringWriter();
    template.process(templateContext, outPutWrite);
    String outPutJson = outPutWrite.toString();
    return outPutJson;
  }

  private Pair<String[], Object[]> transformerData(String templateKey,
      final NettyHttpServletRequest servletRequest) throws TemplateNotFoundException,
      MalformedTemplateNameException, ParseException, IOException, TemplateException {
    String outPutJson = this.doDataMapping(templateKey, servletRequest);
    Map<String, String> dubboParamters =
        JSON.parseObject(outPutJson, new TypeReference<HashMap<String, String>>() {});
    List<String> type = Lists.newArrayList();
    List<Object> value = Lists.newArrayList();
    for (Map.Entry<String, String> entry : dubboParamters.entrySet()) {
      String type_ = entry.getKey();
      String value_ = entry.getValue();
      type.add(type_);
      if (type_.startsWith("java")) {
        value.add(value_);
      } else {
        Map<String, String> value_map =
            JSON.parseObject(value_, new TypeReference<HashMap<String, String>>() {});
        value_map.put("class", type_);
        value.add(value_map);
      }
    }
    String[] typeArray = new String[type.size()];
    type.toArray(typeArray);
    return new ImmutablePair<String[], Object[]>(typeArray, value.toArray());
  }


  @Override
  public String doRpcRemoteCall(final ApiRpcDO rpcDo,
      final NettyHttpServletRequest servletRequest) {
    try {
      final String serviceName = rpcDo.getServiceName();
      final String methodName = rpcDo.getMethodName();
      final String group = rpcDo.getServiceGroup();
      final String version = rpcDo.getServiceVersion();
      ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
      reference.setApplication(applicationConfig);
      reference.setRegistry(registryConfig);
      reference.setInterface(serviceName);
      reference.setGroup(group);
      reference.setGeneric(true);
      reference.setCheck(false);
      reference.setVersion(version);
      ReferenceConfigCache cache = ReferenceConfigCache.getCache();
      GenericService genericService = cache.get(reference);
      String templateKey = this.cacheTemplate(rpcDo);
      Pair<String[], Object[]> typeAndValue = this.transformerData(templateKey, servletRequest);
      Object response =
          genericService.$invoke(methodName, typeAndValue.getLeft(), typeAndValue.getRight());
      return JSON.toJSONString(response);
    } catch (Throwable e) {
      throw new IllegalArgumentException(String.format(
          "service definition is wrong,please check the proto file you update,service is %s, method is %s",
          rpcDo.getServiceName(), rpcDo.getMethodName()), e);
    }

  }


  //
  // /**
  // * <pre>
  // <#assign inputRoot= input.path("$")>
  // [
  // <#list inputRoot.photos as elem>
  // {
  // "id": "${elem.id}",
  // "owner": "${elem.owner}",
  // "title": "${elem.title}",
  // "ispublic": ${elem.ispublic},
  // "isfriend": ${elem.isfriend},
  // "isfamily": ${elem.isfamily}
  // }<#if (elem_has_next)>,</#if>
  // </#list>
  // ]
  // * </pre>
  // *
  // * @throws TemplateException
  // * @throws IOException
  // */
  // public static void main(String[] args) throws IOException, TemplateException {
  // String test = test();
  // System.out.println(test);
  // }
  //
  // private static String test() throws IOException, TemplateException {
  // String freeMarkStr =
  // "[{\"type\":\"io.github.tesla.dubbo.pojo.UserRequest\",\"expression\":\"${jsonStr}\"}]";
  // String freeMark = buildFreemarkerTemplate(freeMarkStr);
  // Template t = new Template(null, new StringReader(freeMark), null);
  // Map<String, Object> map = new HashMap<String, Object>();
  // String body = "{" + //
  // "\"name\": \"linking12\"," + //
  // "\"mobile\": \"18616705342\"," + //
  // "\"idNo\": \"360731198312284358\"" + //
  // "}";//
  //
  // map.put("input", new BodyMapping(body));
  // StringWriter sw = new StringWriter();
  // t.process(map, sw);
  // return sw.toString();
  // }



}
