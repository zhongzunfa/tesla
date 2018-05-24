package io.github.tesla.authz.domain;

import java.io.Serializable;
import java.util.Date;

import io.github.tesla.authz.utils.DateUtils;

public class OauthCode implements Serializable {

  private static final long serialVersionUID = 7861853986708936572L;

  private String code;

  private String username;

  private String clientId;

  private Date createTime = DateUtils.now();

  public String code() {
    return code;
  }

  public OauthCode code(String code) {
    this.code = code;
    return this;
  }

  public String username() {
    return username;
  }

  public OauthCode username(String username) {
    this.username = username;
    return this;
  }

  public String clientId() {
    return clientId;
  }

  public OauthCode clientId(String clientId) {
    this.clientId = clientId;
    return this;
  }

  public OauthCode createTime(Date createTime) {
    this.createTime = createTime;
    return this;
  }

  @Override
  public String toString() {
    return "OauthCode [code=" + code + ", username=" + username + ", clientId=" + clientId
        + ", createTime=" + createTime + "]";
  }

}
