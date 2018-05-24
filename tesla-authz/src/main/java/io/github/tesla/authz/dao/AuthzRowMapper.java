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
package io.github.tesla.authz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import io.github.tesla.authz.domain.AccessToken;
import io.github.tesla.authz.domain.ClientDetails;
import io.github.tesla.authz.domain.OauthCode;
import io.github.tesla.authz.domain.Users;

/**
 * @author liushiming
 * @version Oauth2RowMapper.java, v 0.0.1 2018年2月2日 下午3:16:20 liushiming
 */
public abstract class AuthzRowMapper {

  protected static class AccessTokenRowMapper implements RowMapper<AccessToken> {
    @Override
    public AccessToken mapRow(ResultSet rs, int rowNum) throws SQLException {
      final AccessToken oauthCode = new AccessToken().tokenId(rs.getString("token_id"))//
          .tokenExpiredSeconds(rs.getInt("token_expired_seconds"))//
          .authenticationId(rs.getString("authentication_id"))//
          .username(rs.getString("username"))//
          .clientId(rs.getString("client_id"))//
          .tokenType(rs.getString("token_type"))//
          .refreshTokenExpiredSeconds(rs.getInt("refresh_token_expired_seconds"))//
          .refreshToken(rs.getString("refresh_token"))//
          .createTime(rs.getTimestamp("create_time"));
      return oauthCode;

    }
  }
  protected static class ClientDetailsRowMapper implements RowMapper<ClientDetails> {
    @Override
    public ClientDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
      ClientDetails details = new ClientDetails();
      details.setClientId(rs.getString("client_id"));
      details.setClientSecret(rs.getString("client_secret"));
      details.setName(rs.getString("client_name"));
      details.scope(rs.getString("scope"));
      details.grantTypes(rs.getString("grant_types"));
      details.setRedirectUri(rs.getString("redirect_uri"));
      details.accessTokenValidity(rs.getInt("access_token_validity"));
      details.refreshTokenValidity(rs.getInt("refresh_token_validity"));
      details.createTime(rs.getTimestamp("create_time"));
      details.trusted(rs.getBoolean("trusted"));
      return details;
    }
  }

  protected static class OauthCodeRowMapper implements RowMapper<OauthCode> {
    @Override
    public OauthCode mapRow(ResultSet rs, int rowNum) throws SQLException {
      final OauthCode oauthCode = new OauthCode().clientId(rs.getString("client_id"))//
          .username(rs.getString("username"))//
          .code(rs.getString("code"))//
          .createTime(rs.getTimestamp("create_time"));
      return oauthCode;

    }
  }

  protected static class UsersRowMapper implements RowMapper<Users> {
    @Override
    public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
      Users users = new Users()//
          .userId(rs.getLong("user_id"))//
          .username(rs.getString("username"))//
          .password(rs.getString("password"))//
          .status(rs.getInt("status"));
      return users;
    }
  }

}
