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

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author liushiming
 * @version AuthenticationIdGenerator.java, v 0.0.1 2018年2月2日 下午5:41:14 liushiming
 */
@Component
public class DefaultAuthenticationIdGenerator {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(DefaultAuthenticationIdGenerator.class);


  public String generate(String clientId, String username, String scope) {
    Map<String, String> map = new HashMap<>();
    map.put(OAuth.OAUTH_CLIENT_ID, clientId);
    // check it is client only
    if (!clientId.equals(username)) {
      map.put(OAuth.OAUTH_USERNAME, username);
    }
    if (!OAuthUtils.isEmpty(scope)) {
      map.put(OAuth.OAUTH_SCOPE, scope);
    }

    return digest(map);
  }


  protected String digest(Map<String, String> map) {
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      LOGGER.warn("Digest error", e);
      throw new IllegalStateException(
          "MD5 algorithm not available.  Fatal (should be in the JDK).");
    }

    try {
      byte[] bytes = digest.digest(map.toString().getBytes("UTF-8"));
      return String.format("%032x", new BigInteger(1, bytes));
    } catch (UnsupportedEncodingException e) {
      LOGGER.warn("Encoding error", e);
      throw new IllegalStateException(
          "UTF-8 encoding not available.  Fatal (should be in the JDK).");
    }
  }

}
