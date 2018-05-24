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

import io.github.tesla.ops.api.service.ApiGroupService;
import io.github.tesla.ops.api.vo.ApiGroupVo;
import io.github.tesla.ops.common.BaseController;
import io.github.tesla.ops.common.CommonResponse;
import io.github.tesla.ops.common.Log;
import io.github.tesla.ops.system.domain.PageDO;
import io.github.tesla.ops.utils.Query;

/**
 * @author liushiming
 * @version APIGroupController.java, v 0.0.1 2018年4月11日 下午4:06:43 liushiming
 */
@Controller
@RequestMapping("gateway/apigroup")
public class ApiGroupController extends BaseController {

  private final String prefix = "gateway/apigroup";

  @Autowired
  private ApiGroupService groupService;

  @RequiresPermissions("gateway:apigroup:apigroup")
  @GetMapping()
  public String group() {
    return prefix + "/apigroup";
  }

  @RequiresPermissions("filter:rule:add")
  @GetMapping("/add")
  public String add() {
    return prefix + "/add";
  }


  @Log("查询api分组")
  @RequiresPermissions("gateway:apigroup:apigroup")
  @GetMapping("/list")
  @ResponseBody
  public PageDO<ApiGroupVo> list(@RequestParam Map<String, Object> params) {
    Query query = new Query(params);
    return groupService.queryList(query);
  }


  @RequiresPermissions("gateway:apigroup:edit")
  @GetMapping("/edit/{id}")
  public String edit(@PathVariable("id") Long id, Model model) {
    ApiGroupVo groupVo = groupService.get(id);
    model.addAttribute("group", groupVo);
    return prefix + "/edit";
  }

  @Log("保存分组")
  @ResponseBody
  @PostMapping("/save")
  @RequiresPermissions("gateway:apigroup:add")
  public CommonResponse save(ApiGroupVo groupVo) {
    if (groupService.save(groupVo) > 0) {
      return CommonResponse.ok();
    }
    return CommonResponse.error();
  }

  @Log("更新分组")
  @ResponseBody
  @RequestMapping("/update")
  @RequiresPermissions("gateway:apigroup:edit")
  public CommonResponse update(ApiGroupVo groupVo) {
    if (groupService.update(groupVo) > 0) {
      return CommonResponse.ok();
    }
    return CommonResponse.error();
  }

  @Log("删除分组")
  @PostMapping("/remove")
  @ResponseBody
  @RequiresPermissions("gateway:apigroup:remove")
  public CommonResponse remove(Long groupId) {
    if (groupService.remove(groupId) > 0) {
      return CommonResponse.ok();
    }
    return CommonResponse.error();
  }

  @Log("批量删除分组")
  @PostMapping("/batchRemove")
  @ResponseBody
  @RequiresPermissions("gateway:apigroup:batchRemove")
  public CommonResponse remove(@RequestParam("ids[]") Long[] ids) {
    groupService.batchRemove(ids);
    return CommonResponse.ok();
  }

}
