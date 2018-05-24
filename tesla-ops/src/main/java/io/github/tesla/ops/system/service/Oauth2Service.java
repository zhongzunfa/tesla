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
package io.github.tesla.ops.system.service;

import io.github.tesla.authz.domain.AccessToken;
import io.github.tesla.authz.domain.ClientDetails;
import io.github.tesla.ops.system.domain.PageDO;
import io.github.tesla.ops.utils.Query;

/**
 * @author liushiming
 * @version Oauth2Service.java, v 0.0.1 2018年2月5日 下午3:29:37 liushiming
 */
public interface Oauth2Service {


  ClientDetails get(String clientId);

  int save(ClientDetails client);

  int update(ClientDetails client);

  int remove(String clientId);

  int batchremove(String[] clientIds);


  PageDO<ClientDetails> queryClientDetailsList(Query query);


  PageDO<AccessToken> queryTokenList(Query query);


  int revokeToken(String[] tokenIds);

}
