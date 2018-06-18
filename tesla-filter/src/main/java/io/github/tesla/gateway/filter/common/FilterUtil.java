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
package io.github.tesla.gateway.filter.common;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * @author liushiming
 * @version FilterUtil.java, v 0.0.1 2018年5月26日 上午1:08:29 liushiming
 */
public class FilterUtil {

  private static final Logger logger = LoggerFactory.getLogger("ProxyFilterLog");

  public static List<String> getHeaderValues(HttpMessage httpMessage, String headerName) {
    List<String> list = Lists.newArrayList();
    for (Map.Entry<String, String> header : httpMessage.headers().entries()) {
      if (header.getKey().toLowerCase().equals(headerName.toLowerCase())) {
        list.add(header.getValue());
      }
    }
    return list;
  }

  public static String getRealIp(HttpRequest httpRequest) {
    List<String> headerValues = getHeaderValues(httpRequest, "X-Real-IP");
    if (headerValues.size() > 0) {
      return headerValues.get(0);
    } else {
      return null;
    }

  }

  public static HttpResponse createResponse(HttpResponseStatus httpResponseStatus,
      HttpRequest originalRequest, String... reason) {
    HttpHeaders httpHeaders = new DefaultHttpHeaders();
    httpHeaders.add("Transfer-Encoding", "chunked");
    HttpResponse httpResponse;
    if (reason != null && reason.length > 0) {
      final ResponseResult responseResult = new ResponseResult(httpResponseStatus, Arrays.toString(reason));
      final String responseBody = JsonUtils.serializeToJson(responseResult);
      httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpResponseStatus,
          Unpooled.copiedBuffer(responseBody, CharsetUtil.UTF_8));
      httpHeaders.set("Context-Type", "application/json;charset=UTF-8");
    } else {
      httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpResponseStatus);
    }
    List<String> originHeader = FilterUtil.getHeaderValues(originalRequest, "Origin");
    if (originHeader.size() > 0) {
      httpHeaders.set("Access-Control-Allow-Credentials", "true");
      httpHeaders.set("Access-Control-Allow-Origin", originHeader.get(0));
    }
    httpResponse.headers().add(httpHeaders);
    return httpResponse;
  }

  public static void writeFilterLog(Class<?> type, String reason, Throwable... cause) {
    if (cause != null) {
      logger.error("execute filter:" + type + " occur error, reason is:" + reason, cause[0]);
    } else {
      logger.info("execute filter:" + type + " occur error, reason is:" + reason);
    }
  }

  static class ResponseResult {
    private Result result;
    private Object content;

    ResponseResult(HttpResponseStatus responseStatus, String message, Object content) {
      this.result = new Result(responseStatus.code(), message);
      this.content = content;
    }

    public ResponseResult(HttpResponseStatus responseStatus, Object content) {
      this(responseStatus, responseStatus.reasonPhrase(), content);
    }

    public static ResponseResult success() {
      return success(null);
    }

    public static ResponseResult success(Object data) {
      return new ResponseResult(HttpResponseStatus.OK, data);
    }

    public static ResponseResult success(String message, Object data) {
      return new ResponseResult(HttpResponseStatus.OK, message, data);
    }

    public static ResponseResult error(HttpResponseStatus reponseStatus, String message) {
      return new ResponseResult(reponseStatus, message, null);
    }

    public static ResponseResult error(HttpResponseStatus reponseStatus) {
      return new ResponseResult(reponseStatus, reponseStatus.reasonPhrase(), null);
    }

    public static ResponseResult error(HttpResponseStatus reponseStatus, String message, Object data) {
      return new ResponseResult(reponseStatus, message, data);
    }
  }

  static class Result {
    private int code;
    private String message;
    Result(int code, String message) {
      this.code = code;
      this.message = message;
    }

    public int getCode() {
      return code;
    }

    public void setCode(int code) {
      this.code = code;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }
}
