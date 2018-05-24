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
package io.github.tesla.ops.system.controller;

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

import io.github.tesla.authz.domain.AccessToken;
import io.github.tesla.authz.domain.ClientDetails;
import io.github.tesla.ops.common.CommonResponse;
import io.github.tesla.ops.common.Log;
import io.github.tesla.ops.system.domain.PageDO;
import io.github.tesla.ops.system.service.Oauth2Service;
import io.github.tesla.ops.utils.Query;


@RequestMapping("/sys/oauth2")
@Controller
public class Oauth2Controller {

  private String prefix = "oauth2";

  @Autowired
  private Oauth2Service oauth2Service;


  @RequiresPermissions("sys:oauth2:listToken")
  @GetMapping("/token")
  String token() {
    return prefix + "/token";
  }

  @RequiresPermissions("sys:oauth2:listclient")
  @GetMapping("/client")
  String client() {
    return prefix + "/client";
  }

  @Log("添加客户端")
  @RequiresPermissions("sys:oauth2:add")
  @GetMapping("/add")
  String add() {
    return prefix + "/add";
  }

  @Log("编辑客户端")
  @RequiresPermissions("sys:oauth2:edit")
  @GetMapping("/edit/{id}")
  String edit(@PathVariable("id") String id, Model model) {
    ClientDetails client = oauth2Service.get(id);
    model.addAttribute("client", client);
    return prefix + "/edit";
  }

  @Log("保存客户端")
  @RequiresPermissions("sys:oauth2:add")
  @PostMapping("/save")
  @ResponseBody()
  CommonResponse save(ClientDetails client) {
    if (oauth2Service.save(client) > 0) {
      return CommonResponse.ok();
    } else {
      return CommonResponse.error(1, "保存失败");
    }
  }

  @Log("更新客户端")
  @RequiresPermissions("sys:oauth2:edit")
  @PostMapping("/update")
  @ResponseBody()
  CommonResponse update(ClientDetails client) {
    if (oauth2Service.update(client) > 0) {
      return CommonResponse.ok();
    } else {
      return CommonResponse.error(1, "保存失败");
    }
  }

  @Log("删除客户端")
  @RequiresPermissions("sys:oauth2:remove")
  @PostMapping("/remove")
  @ResponseBody()
  CommonResponse save(String id) {
    if (oauth2Service.remove(id) > 0) {
      return CommonResponse.ok();
    } else {
      return CommonResponse.error(1, "删除失败");
    }
  }

  @RequiresPermissions("sys:oauth2:batchRemove")
  @Log("批量删除客户端")
  @PostMapping("/batchRemove")
  @ResponseBody
  CommonResponse batchRemove(@RequestParam("ids[]") String[] ids) {
    int r = oauth2Service.batchremove(ids);
    if (r > 0) {
      return CommonResponse.ok();
    }
    return CommonResponse.error();
  }

  @ResponseBody
  @GetMapping("/listClient")
  @RequiresPermissions("sys:oauth2:listclient")
  PageDO<ClientDetails> listClients(@RequestParam Map<String, Object> params) {
    Query query = new Query(params);
    PageDO<ClientDetails> page = oauth2Service.queryClientDetailsList(query);
    return page;
  }

  @ResponseBody
  @GetMapping("/listToken")
  @RequiresPermissions("sys:oauth2:listToken")
  PageDO<AccessToken> listTokens(@RequestParam Map<String, Object> params) {
    Query query = new Query(params);
    PageDO<AccessToken> page = oauth2Service.queryTokenList(query);
    return page;
  }


  @RequiresPermissions("sys:oauth2:batchRemove")
  @Log("批量清除Token")
  @PostMapping("/batchRevoke")
  @ResponseBody
  CommonResponse bachRevoke(@RequestParam("ids[]") String[] tokenIds) {
    int r = oauth2Service.revokeToken(tokenIds);
    if (r > 0) {
      return CommonResponse.ok();
    }
    return CommonResponse.error();
  }

}
