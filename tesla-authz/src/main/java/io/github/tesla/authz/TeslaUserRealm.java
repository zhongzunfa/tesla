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
package io.github.tesla.authz;

import java.util.List;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import io.github.tesla.authz.dao.AuthzUserDao;
import io.github.tesla.authz.domain.Users;

/**
 * @author liushiming
 * @version TeslaUserRealm.java, v 0.0.1 2018年1月31日 下午4:14:29 liushiming
 */
public class TeslaUserRealm extends AuthorizingRealm {


  private final AuthzUserDao userDao;

  public TeslaUserRealm(AuthzUserDao userDao) {
    this.userDao = userDao;
  }


  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    if (principals == null) {
      throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
    }
    Long userId = (Long) principals.getPrimaryPrincipal();
    List<String> permissions = userDao.findPermissonByUserId(userId);
    List<String> roles = userDao.findRoleByUserId(userId);
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    info.addRoles(roles);
    info.addStringPermissions(permissions);
    return info;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
    UsernamePasswordToken upToken = (UsernamePasswordToken) token;
    String username = upToken.getUsername();
    if (username == null) {
      throw new AccountException("Null usernames are not allowed by this realm.");
    }
    Users user = userDao.findByUserNamed(username);
    Long userId = user.userId();
    String password = user.password();
    int status = user.status();
    if (password == null) {
      throw new UnknownAccountException("No account found for " + username);
    }
    if (!password.equals(new String((char[]) token.getCredentials()))) {
      throw new IncorrectCredentialsException("Password is not right for " + username);
    }
    if (status == 0) {
      throw new LockedAccountException("account is locked for user " + username);
    }
    SimpleAuthenticationInfo info =
        new SimpleAuthenticationInfo(userId, password.toCharArray(), username);
    info.setCredentialsSalt(ByteSource.Util.bytes(username));
    return info;
  }



}
