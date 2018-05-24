package io.github.tesla.gateway.netty.filter.request;

import io.github.tesla.common.RequestFilterTypeEnum;
import io.github.tesla.gateway.netty.filter.AbstractCommonFilter;
import io.github.tesla.gateway.netty.servlet.NettyHttpServletRequest;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;


public abstract class HttpRequestFilter extends AbstractCommonFilter {

  /**
   * @param servletRequest 对FullHttpRequest的Servlet封装，能够重复操作
   * @param httpObject 真正的Http数据请求对象
   * 
   *        <pre>
   * 为什么把FullHttpRequest封装成NettyHttpServletRequest封装：
   * 1：方便Filter的读取
   * 2：FullHttpRequest的内容Content只能读取一次，如果一个Filter读取过了会导致以后的Filter取值取不到
   * 3：httpObject需要谨慎操作，不能做过多的修改
   *        </pre>
   */
  public abstract HttpResponse doFilter(NettyHttpServletRequest servletRequest,
      HttpObject realHttpObject);

  public abstract RequestFilterTypeEnum filterType();

  @Override
  public String filterName() {
    return filterType().name();
  }

}
