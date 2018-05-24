package io.github.tesla.authz.domain;

import java.util.Date;

import org.apache.oltu.oauth2.common.domain.client.BasicClientInfo;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import io.github.tesla.authz.utils.DateUtils;

public class ClientDetails extends BasicClientInfo {

  private String scope;

  private String grantTypes;
  /**
   * access_token 的有效时长, 单位: 秒. 若不填或为空(null)则使用默认值: 12小时
   */
  private Integer accessTokenValidity;
  /**
   * refresh_token的 有效时长, 单位: 秒 若不填或为空(null)则使用默认值: 30天
   */
  private Integer refreshTokenValidity;
  /**
   * 该 客户端是否为授信任的, 若为信任的,, 则在 grant_type = authorization_code 时将跳过用户同意/授权 步骤
   */
  private boolean trusted = false;
  /**
   * 创建时间
   */
  private Date createTime = DateUtils.now();


  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getGrantTypes() {
    return grantTypes;
  }

  public void setGrantTypes(String grantTypes) {
    this.grantTypes = grantTypes;
  }

  public Integer getAccessTokenValidity() {
    return accessTokenValidity;
  }

  public void setAccessTokenValidity(Integer accessTokenValidity) {
    this.accessTokenValidity = accessTokenValidity;
  }

  public Integer getRefreshTokenValidity() {
    return refreshTokenValidity;
  }

  public void setRefreshTokenValidity(Integer refreshTokenValidity) {
    this.refreshTokenValidity = refreshTokenValidity;
  }

  public boolean isTrusted() {
    return trusted;
  }

  public void setTrusted(boolean trusted) {
    this.trusted = trusted;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String scope() {
    return scope;
  }

  public ClientDetails scope(String scope) {
    this.scope = scope;
    return this;
  }

  public String grantTypes() {
    return grantTypes;
  }

  public ClientDetails grantTypes(String grantTypes) {
    this.grantTypes = grantTypes;
    return this;
  }

  public Integer accessTokenValidity() {
    return accessTokenValidity;
  }

  public ClientDetails accessTokenValidity(Integer accessTokenValidity) {
    this.accessTokenValidity = accessTokenValidity;
    return this;
  }

  public Integer refreshTokenValidity() {
    return refreshTokenValidity;
  }

  public ClientDetails refreshTokenValidity(Integer refreshTokenValidity) {
    this.refreshTokenValidity = refreshTokenValidity;
    return this;
  }

  public boolean trusted() {
    return trusted;
  }

  public ClientDetails trusted(boolean trusted) {
    this.trusted = trusted;
    return this;
  }

  public Date createTime() {
    return createTime;
  }

  public ClientDetails createTime(Date createTime) {
    this.createTime = createTime;
    return this;
  }

  public boolean supportRefreshToken() {
    return this.grantTypes != null && this.grantTypes.contains(GrantType.REFRESH_TOKEN.toString());
  }

  @Override
  public String toString() {
    return "ClientDetails [scope=" + scope + ", grantTypes=" + grantTypes + ", accessTokenValidity="
        + accessTokenValidity + ", refreshTokenValidity=" + refreshTokenValidity + ", trusted="
        + trusted + ", createTime=" + createTime + "]";
  }

}
