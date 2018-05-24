package io.github.tesla.authz.domain;

import java.io.Serializable;
import java.util.Date;

import io.github.tesla.authz.utils.DateUtils;

public class AccessToken implements Serializable {
  private static final long serialVersionUID = 7336709167855003668L;

  private static final String BEARER_TYPE = "Bearer";

  /**
   * 默认的 refresh_token 的有效时长: 30天
   */
  private final static int REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 30;

  /**
   * 默认的 access_token 的有效时长: 12小时
   */
  private final static int ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 12;


  private String tokenId;

  private String username;

  private String clientId;

  private String authenticationId;

  private String refreshToken;

  private String tokenType = BEARER_TYPE;

  private int tokenExpiredSeconds = ACCESS_TOKEN_VALIDITY_SECONDS;

  private int refreshTokenExpiredSeconds = REFRESH_TOKEN_VALIDITY_SECONDS;

  private Date createTime = DateUtils.now();

  public boolean tokenExpired() {
    final long time = createTime.getTime() + (this.tokenExpiredSeconds * 1000L);
    return time < DateUtils.now().getTime();
  }


  public boolean refreshTokenExpired() {
    final long time = createTime.getTime() + (this.refreshTokenExpiredSeconds * 1000L);
    return time < DateUtils.now().getTime();
  }


  public long currentTokenExpiredSeconds() {
    if (tokenExpired()) {
      return -1;
    }
    final long time = createTime.getTime() + (this.tokenExpiredSeconds * 1000L);
    return (time - DateUtils.now().getTime()) / 1000L;
  }

  public AccessToken updateByClientDetails(ClientDetails clientDetails) {
    this.clientId = clientDetails.getClientId();

    final Integer accessTokenValidity = clientDetails.accessTokenValidity();
    if (accessTokenValidity != null && accessTokenValidity > 0) {
      this.tokenExpiredSeconds = accessTokenValidity;
    }

    final Integer refreshTokenValidity = clientDetails.refreshTokenValidity();
    if (refreshTokenValidity != null && refreshTokenValidity > 0) {
      this.refreshTokenExpiredSeconds = refreshTokenValidity;
    }

    return this;
  }

  public String tokenId() {
    return tokenId;
  }

  public AccessToken tokenId(String tokenId) {
    this.tokenId = tokenId;
    return this;
  }

  public String username() {
    return username;
  }

  public AccessToken username(String username) {
    this.username = username;
    return this;
  }

  public String clientId() {
    return clientId;
  }

  public AccessToken clientId(String clientId) {
    this.clientId = clientId;
    return this;
  }

  public String refreshToken() {
    return refreshToken;
  }

  public AccessToken refreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
  }

  public String tokenType() {
    return tokenType;
  }

  public AccessToken tokenType(String tokenType) {
    this.tokenType = tokenType;
    return this;
  }

  public int tokenExpiredSeconds() {
    return tokenExpiredSeconds;
  }

  public AccessToken tokenExpiredSeconds(int tokenExpiredSeconds) {
    this.tokenExpiredSeconds = tokenExpiredSeconds;
    return this;
  }

  public int refreshTokenExpiredSeconds() {
    return refreshTokenExpiredSeconds;
  }

  public AccessToken refreshTokenExpiredSeconds(int refreshTokenExpiredSeconds) {
    this.refreshTokenExpiredSeconds = refreshTokenExpiredSeconds;
    return this;
  }


  public String authenticationId() {
    return authenticationId;
  }

  public AccessToken authenticationId(String authenticationId) {
    this.authenticationId = authenticationId;
    return this;
  }

  public AccessToken createTime(Date createTime) {
    this.createTime = createTime;
    return this;
  }

  public AccessToken cloneMe() {
    return new AccessToken().username(username).clientId(clientId).tokenType(tokenType);
  }

  public String getTokenId() {
    return tokenId;
  }


  public void setTokenId(String tokenId) {
    this.tokenId = tokenId;
  }


  public String getUsername() {
    return username;
  }


  public void setUsername(String username) {
    this.username = username;
  }


  public String getClientId() {
    return clientId;
  }


  public void setClientId(String clientId) {
    this.clientId = clientId;
  }


  public String getAuthenticationId() {
    return authenticationId;
  }


  public void setAuthenticationId(String authenticationId) {
    this.authenticationId = authenticationId;
  }


  public String getRefreshToken() {
    return refreshToken;
  }


  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }


  public String getTokenType() {
    return tokenType;
  }


  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }


  public int getTokenExpiredSeconds() {
    return tokenExpiredSeconds;
  }


  public void setTokenExpiredSeconds(int tokenExpiredSeconds) {
    this.tokenExpiredSeconds = tokenExpiredSeconds;
  }


  public int getRefreshTokenExpiredSeconds() {
    return refreshTokenExpiredSeconds;
  }


  public void setRefreshTokenExpiredSeconds(int refreshTokenExpiredSeconds) {
    this.refreshTokenExpiredSeconds = refreshTokenExpiredSeconds;
  }


  public Date getCreateTime() {
    return createTime;
  }


  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }


  @Override
  public String toString() {
    return "AccessToken [tokenId=" + tokenId + ", username=" + username + ", clientId=" + clientId
        + ", authenticationId=" + authenticationId + ", refreshToken=" + refreshToken
        + ", tokenType=" + tokenType + ", tokenExpiredSeconds=" + tokenExpiredSeconds
        + ", refreshTokenExpiredSeconds=" + refreshTokenExpiredSeconds + ", createTime="
        + createTime + "]";
  }



}
