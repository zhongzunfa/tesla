/*
 * Copyright (c) 2018 DISID CORPORATION S.L.
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

package com.bkjk.gateway.filter.request;

import com.google.common.collect.Maps;
import io.github.tesla.gateway.filter.UserRequestFilter;
import io.github.tesla.gateway.filter.common.GroovyCompiler;
import io.github.tesla.gateway.filter.servlet.NettyHttpServletRequest;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;


public class GroovySampleRequestFilter extends UserRequestFilter {

  @Override
  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpObject realHttpObject) {
     
     
     
  }

}

