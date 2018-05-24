package io.github.tesla.ops.common;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {

  public static Long getUserId() {
    return (Long) SecurityUtils.getSubject().getPrincipal();
  }

  public static String getUsername() {
    return SecurityUtils.getSubject().getPrincipals().getRealmNames().iterator().next();
  }
}
