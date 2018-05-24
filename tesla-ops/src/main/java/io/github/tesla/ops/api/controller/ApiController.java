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
package io.github.tesla.ops.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;

import io.github.tesla.common.RouteType;
import io.github.tesla.ops.api.service.ApiGroupService;
import io.github.tesla.ops.api.service.ApiService;
import io.github.tesla.ops.api.service.ProtobufService;
import io.github.tesla.ops.api.vo.ApiGroupVo;
import io.github.tesla.ops.api.vo.ApiVo;
import io.github.tesla.ops.common.BaseController;
import io.github.tesla.ops.common.CommonResponse;
import io.github.tesla.ops.common.Log;
import io.github.tesla.ops.common.TeslaException;
import io.github.tesla.ops.system.domain.PageDO;
import io.github.tesla.ops.utils.FileType;
import io.github.tesla.ops.utils.Query;

/**
 * @author liushiming
 * @version Api.java, v 0.0.1 2018年1月9日 上午11:19:14 liushiming
 */
@Controller
@RequestMapping("gateway/api")
public class ApiController extends BaseController {

  private final String prefix = "gateway/api";

  @Autowired
  private ApiService apiService;

  @Autowired
  private ApiGroupService groupService;

  @Autowired
  private ProtobufService protobufService;

  @RequiresPermissions("gateway:api:api")
  @GetMapping()
  public String api() {
    return prefix + "/api";
  }

  @RequiresPermissions("gateway:api:add")
  @GetMapping("/add")
  public String add(Model model) {
    List<ApiGroupVo> apiGroups = groupService.list(Maps.newHashMap());
    model.addAttribute("apiGroups", apiGroups);
    model.addAttribute("apiRoutes", RouteType.values());
    return prefix + "/add";
  }

  @RequiresPermissions("gateway:api:edit")
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable("id") Long id, Model model) {
    ApiVo apiVO = apiService.get(id);
    List<ApiGroupVo> apiGroups = groupService.list(Maps.newHashMap());
    model.addAttribute("apiGroups", apiGroups);
    model.addAttribute("api", apiVO);
    model.addAttribute("apiRoutes", RouteType.values());
    return prefix + "/edit";
  }

  @Log("保存路由")
  @RequiresPermissions("gateway:api:add")
  @PostMapping("/save")
  @ResponseBody()
  public CommonResponse save(ApiVo apiVO,
      @RequestParam(name = "zipFile", required = false) MultipartFile zipFile) {
    try {
      // grpc路由
      if (zipFile != null) {
        InputStream directoryZipStream = zipFile.getInputStream();
        CommonResponse response = judgeFileType(directoryZipStream, "zip");
        if (response != null) {
          return response;
        } else {
          byte[] protoContext = protobufService.compileDirectoryProto(zipFile);
          apiVO.setProtoContext(protoContext);
        }
      }
      apiService.save(apiVO);
    } catch (IOException e) {
      throw new TeslaException("保存路由失败", e);
    }
    return CommonResponse.ok();
  }

  @Log("查询路由")
  @RequiresPermissions("gateway:api:api")
  @GetMapping("/list")
  @ResponseBody
  public PageDO<ApiVo> list(@RequestParam Map<String, Object> params) {
    Query query = new Query(params);
    return apiService.queryList(query);
  }


  @Log("更新路由")
  @RequiresPermissions("gateway:api:edit")
  @PostMapping("/update")
  @ResponseBody()
  public CommonResponse update(ApiVo apiVO,
      @RequestParam(name = "zipFile", required = false) MultipartFile zipFile) {
    try {
      if (zipFile != null) {
        InputStream directoryZipStream = zipFile.getInputStream();
        CommonResponse response = judgeFileType(directoryZipStream, "zip");
        if (response != null) {
          return response;
        } else {
          byte[] protoContext = protobufService.compileDirectoryProto(zipFile);
          apiVO.setProtoContext(protoContext);
        }
      }
      apiService.update(apiVO);
    } catch (IOException e) {
      throw new TeslaException("保存路由失败", e);
    }
    return CommonResponse.ok();
  }

  @Log("删除路由")
  @RequiresPermissions("gateway:api:remove")
  @PostMapping("/remove")
  @ResponseBody()
  public CommonResponse save(Long id) {
    if (apiService.remove(id) > 0) {
      return CommonResponse.ok();
    } else {
      return CommonResponse.error(1, "删除失败");
    }
  }

  @RequiresPermissions("gateway:api:batchRemove")
  @Log("批量删除路由")
  @PostMapping("/batchRemove")
  @ResponseBody
  public CommonResponse batchRemove(@RequestParam("ids[]") Long[] ids) {
    int response = apiService.batchRemove(ids);
    if (response > 0) {
      return CommonResponse.ok();
    }
    return CommonResponse.error();
  }


  private CommonResponse judgeFileType(InputStream inpustream, String type) throws IOException {
    String fileType = FileType.calculateFileHexString(inpustream);
    if (!type.equals(fileType)) {
      return CommonResponse.error(1, "只能上传" + type + "类型文件");
    } else {
      return null;
    }
  }

}
