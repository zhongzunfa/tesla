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
package io.github.tesla.authz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author liushiming
 * @version OauthDispatcherController.java, v 0.0.1 2018年2月9日 下午2:20:08 liushiming
 */
@Controller
@RequestMapping("oauth")
public class OauthDispatcherController {


  @RequestMapping(value = "/default_redirect_url", method = {RequestMethod.POST, RequestMethod.GET})
  String oauthLogin() {
    return "/oauth/default_redirect_url";
  }

  @RequestMapping(value = "/oauth_login", method = {RequestMethod.POST, RequestMethod.GET})
  String defaultRedirect() {
    return "/oauth/oauth_login";
  }

  @RequestMapping(value = "/oauth_approval", method = {RequestMethod.POST, RequestMethod.GET})
  String approval() {
    return "/oauth/oauth_approval";
  }

}
