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
package io.github.tesla.springcloud.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.github.tesla.springcloud.pojo.User;
import io.github.tesla.springcloud.pojo.Users;

/**
 * @author liushiming
 * @version UserGroupController.java, v 0.0.1 2018年5月30日 下午5:26:59 liushiming
 */
@RestController
@RequestMapping(value = "user")
public class UserGroupController {

  @RequestMapping(value = "single", method = RequestMethod.POST)
  public User user(@RequestBody User user) {
    return user;
  }

  @RequestMapping(value = "list", method = RequestMethod.POST)
  public Users users(@RequestBody Users users) {
    return users;
  }
}
