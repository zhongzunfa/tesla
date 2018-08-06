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

package io.github.tesla.gateway.netty.filter.response;

import java.io.IOException;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import io.github.tesla.common.ResponseFilterTypeEnum;
import io.github.tesla.gateway.filter.servlet.NettyHttpServletRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.CharsetUtil;

/**
 * ClassName:XssHttpRequestFilter <br/>
 * 
 * @author liushiming
 * @version
 * @since JDK 10
 * @see
 */
public class XssHttpResponseFilter extends HttpResponseFilter {

  private org.owasp.validator.html.Policy policy;

  public XssHttpResponseFilter() {
    try {
      Resource[] resources =
          new PathMatchingResourcePatternResolver().getResources("classpath*:antisamy.xml");
      for (Resource resource : resources) {
        policy = org.owasp.validator.html.Policy.getInstance(resource.getURL());
      }
    } catch (IOException | PolicyException e) {
      e.printStackTrace();
    }


  }

  @Override
  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpResponse httpResponse) {
    if (policy != null) {
      FullHttpResponse fullHttpResonse = (FullHttpResponse) httpResponse;
      ByteBuf responseBuffer = fullHttpResonse.content();
      String responseStr = responseBuffer.toString(CharsetUtil.UTF_8);
      AntiSamy antiSamy = new AntiSamy();
      try {
        CleanResults cleanresult = antiSamy.scan(responseStr, policy);
        ByteBuf bodyContent = Unpooled.copiedBuffer(cleanresult.getCleanHTML(), CharsetUtil.UTF_8);
        fullHttpResonse.content().clear().writeBytes(bodyContent);
        HttpUtil.setContentLength(fullHttpResonse, bodyContent.readerIndex());
      } catch (ScanException | PolicyException e) {
        e.printStackTrace();
      }
      return fullHttpResonse;
    }
    return httpResponse;
  }

  @Override
  public ResponseFilterTypeEnum filterType() {
    return ResponseFilterTypeEnum.XssHttpRequestFilter;
  }



}

