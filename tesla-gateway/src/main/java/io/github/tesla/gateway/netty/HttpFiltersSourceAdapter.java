package io.github.tesla.gateway.netty;

import io.github.tesla.gateway.metrics.MetricsExporter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;


public class HttpFiltersSourceAdapter {

  public HttpFiltersAdapter filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx,
      MetricsExporter metricExporter) {
    return new HttpFiltersAdapter(originalRequest, ctx, metricExporter);
  }

  public int getMaximumRequestBufferSizeInBytes() {
    return 512 * 1024;
  }

  public int getMaximumResponseBufferSizeInBytes() {
    return 512 * 1024;
  }

}
