package io.github.tesla.gateway.netty.filter;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.github.tesla.gateway.cache.GroovyFilterCacheComponent;
import io.github.tesla.gateway.config.SpringContextHolder;
import io.github.tesla.gateway.netty.filter.request.HttpRequestFilter;
import io.github.tesla.gateway.netty.servlet.NettyHttpServletRequest;
import io.github.tesla.gateway.utils.ClassUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;

public class HttpRequestFilterChain {
  private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestFilterChain.class);
  public static final Map<String, HttpRequestFilter> filters = Maps.newTreeMap();
  private static final HttpRequestFilterChain filterChain = new HttpRequestFilterChain();
  private static final String REQUEST_FILTER_PACKAGENAME =
      "io.github.tesla.gateway.netty.filter.request";

  static {
    Set<Class<?>> requestFilterClazzs = ClassUtils.findAllClasses(REQUEST_FILTER_PACKAGENAME);
    for (Class<?> clazz : requestFilterClazzs) {
      if (HttpRequestFilter.class.isAssignableFrom(clazz)
          && !Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface()) {
        try {
          HttpRequestFilter filter = (HttpRequestFilter) clazz.newInstance();
          filters.put(filter.filterName(), filter);
        } catch (Throwable e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static HttpRequestFilterChain requestFilterChain() {
    GroovyFilterCacheComponent filterComponent =
        SpringContextHolder.getBean(GroovyFilterCacheComponent.class);
    Map<String, String> groovyFilters = filterComponent.getRequestGroovyCode();
    for (Map.Entry<String, String> entry : groovyFilters.entrySet()) {
      String name = entry.getKey();
      String filter = entry.getValue();
      Class<?> clazz = GroovyCompiler.compile(filter);
      try {
        HttpRequestFilter requestFilter = (HttpRequestFilter) clazz.newInstance();
        filters.put(name, requestFilter);
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
    List<String> keys = filterComponent.getRequestDeleteKey();
    for (String key : keys) {
      filters.remove(key);
    }
    return filterChain;
  }


  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpObject httpObject,
      ChannelHandlerContext channelHandlerContext) {
    List<Map.Entry<String, HttpRequestFilter>> list = Lists.newArrayList(filters.entrySet());
    Collections.sort(list, new Comparator<Map.Entry<String, HttpRequestFilter>>() {
      public int compare(Entry<String, HttpRequestFilter> o1, Entry<String, HttpRequestFilter> o2) {
        return o1.getValue().filterType().order() - o2.getValue().filterType().order();
      }

    });

    for (Iterator<Map.Entry<String, HttpRequestFilter>> it = list.iterator(); it.hasNext();) {
      HttpRequestFilter filter = it.next().getValue();
      LOGGER.debug("do filter,the name is:" + filter.filterName());
      HttpResponse response = filter.doFilter(servletRequest, httpObject);
      // 如果一个filter有返回值，将会中断下一个filter，这里需要注意filter的顺序，默认grpc->dubbo
      if (response != null) {
        LOGGER.debug("hit " + filter);
        return response;
      }
    }
    return null;
  }
}
