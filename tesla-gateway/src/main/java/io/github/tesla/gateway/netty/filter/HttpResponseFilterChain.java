package io.github.tesla.gateway.netty.filter;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.tesla.gateway.filter.servlet.NettyHttpServletRequest;
import io.github.tesla.gateway.netty.filter.response.HttpResponseFilter;
import io.github.tesla.gateway.utils.ClassUtils;
import io.netty.handler.codec.http.HttpResponse;


public class HttpResponseFilterChain {
  public static final Map<String, HttpResponseFilter> filters = Maps.newTreeMap();
  private static HttpResponseFilterChain filterChain = new HttpResponseFilterChain();
  private static final String RESPONSE_FILTER_PACKAGENAME =
      "io.github.tesla.gateway.netty.filter.response";

  static {
    Set<Class<?>> requestFilterClazzs = ClassUtils.findAllClasses(RESPONSE_FILTER_PACKAGENAME);
    for (Class<?> clazz : requestFilterClazzs) {
      if (HttpResponseFilter.class.isAssignableFrom(clazz)
          && !Modifier.isAbstract(clazz.getModifiers()) && !clazz.isInterface()) {
        try {
          HttpResponseFilter filter = (HttpResponseFilter) clazz.newInstance();
          filters.put(filter.filterName(), filter);
        } catch (Throwable e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static HttpResponseFilterChain responseFilterChain() {
    return filterChain;
  }


  public HttpResponse doFilter(NettyHttpServletRequest servletRequest, HttpResponse httpResponse) {
    List<Map.Entry<String, HttpResponseFilter>> list = Lists.newArrayList(filters.entrySet());
    Collections.sort(list, new Comparator<Map.Entry<String, HttpResponseFilter>>() {
      public int compare(Entry<String, HttpResponseFilter> o1,
          Entry<String, HttpResponseFilter> o2) {
        return o1.getValue().filterType().order() - o2.getValue().filterType().order();
      }

    });
    for (Iterator<Map.Entry<String, HttpResponseFilter>> it = list.iterator(); it.hasNext();) {
      HttpResponseFilter filter = it.next().getValue();
      HttpResponse response = filter.doFilter(servletRequest, httpResponse);
      if (response != null) {
        return response;
      }
    }
    return null;
  }
}
