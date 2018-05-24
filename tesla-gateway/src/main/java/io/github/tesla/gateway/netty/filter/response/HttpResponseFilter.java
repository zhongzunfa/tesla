package io.github.tesla.gateway.netty.filter.response;

import io.github.tesla.common.ResponseFilterTypeEnum;
import io.github.tesla.gateway.netty.filter.AbstractCommonFilter;
import io.github.tesla.gateway.netty.servlet.NettyHttpServletRequest;
import io.netty.handler.codec.http.HttpResponse;


public abstract class HttpResponseFilter extends AbstractCommonFilter {

  public abstract HttpResponse doFilter(NettyHttpServletRequest servletRequest,
      HttpResponse httpResponse);

  public abstract ResponseFilterTypeEnum filterType();

  @Override
  public String filterName() {
    return filterType().name();
  }
}
