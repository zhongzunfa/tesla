package io.github.tesla.authz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import javax.sql.DataSource;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.issuer.ValueGenerator;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import io.github.tesla.authz.dao.AuthzUserDao;

@Configuration
public class AuthzConfig {

  @Bean
  public org.apache.shiro.cache.ehcache.EhCacheManager shiroEhcacheManager(
      org.springframework.cache.ehcache.EhCacheCacheManager cacheManager) {
    org.apache.shiro.cache.ehcache.EhCacheManager ehcacheManager =
        new org.apache.shiro.cache.ehcache.EhCacheManager();
    ehcacheManager.setCacheManager(cacheManager.getCacheManager());
    return ehcacheManager;
  }

  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource, true);
  }

  @Bean
  public TeslaUserRealm userRealm(EhCacheManager ehcacheManager, DataSource dataSource,
      AuthzUserDao userDao) {
    TeslaUserRealm userRealm = new TeslaUserRealm(userDao);
    userRealm.setCacheManager(ehcacheManager);
    return userRealm;
  }

  @Bean
  public SessionDAO sessionDAO() {
    MemorySessionDAO sessionDAO = new MemorySessionDAO();
    return sessionDAO;
  }

  @Bean
  public SessionManager sessionManager() {
    DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
    Collection<SessionListener> listeners = new ArrayList<SessionListener>();
    listeners.add(new TeslaSessionListener());
    sessionManager.setSessionListeners(listeners);
    sessionManager.setSessionDAO(sessionDAO());
    return sessionManager;
  }

  @Bean
  public SecurityManager securityManager(TeslaUserRealm userRealm, EhCacheManager cacheManager) {
    DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
    manager.setRealm(userRealm);
    manager.setCacheManager(cacheManager);
    manager.setSessionManager(sessionManager());
    return manager;
  }

  @Bean
  public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
    shiroFilterFactoryBean.setSecurityManager(securityManager);
    shiroFilterFactoryBean.setLoginUrl("/login");
    shiroFilterFactoryBean.setSuccessUrl("/index");
    shiroFilterFactoryBean.setUnauthorizedUrl("/403");
    LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
    filterChainDefinitionMap.put("/css/**", "anon");
    filterChainDefinitionMap.put("/js/**", "anon");
    filterChainDefinitionMap.put("/fonts/**", "anon");
    filterChainDefinitionMap.put("/img/**", "anon");
    filterChainDefinitionMap.put("/docs/**", "anon");
    filterChainDefinitionMap.put("/druid/**", "anon");
    filterChainDefinitionMap.put("/upload/**", "anon");
    filterChainDefinitionMap.put("/files/**", "anon");
    filterChainDefinitionMap.put("/oauth/**", "anon");
    filterChainDefinitionMap.put("/logout", "logout");
    filterChainDefinitionMap.put("/", "anon");
    filterChainDefinitionMap.put("/**", "authc");
    shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    return shiroFilterFactoryBean;
  }

  @Bean("lifecycleBeanPostProcessor")
  public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }

  @Bean
  public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
    DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
    proxyCreator.setProxyTargetClass(true);
    return proxyCreator;
  }

  @Bean
  public ShiroDialect shiroDialect() {
    return new ShiroDialect();
  }

  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
      @Qualifier("securityManager") SecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor =
        new AuthorizationAttributeSourceAdvisor();
    authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
    return authorizationAttributeSourceAdvisor;
  }

  /*** otlu oauth2 **/
  @Bean
  public ValueGenerator ValueGenerator() {
    return new MD5Generator();
  }

  @Bean
  public OAuthIssuer oAuthIssuer(ValueGenerator valueGenerator) {
    return new OAuthIssuerImpl(valueGenerator);
  }


  /*** otlu oauth2 call back url **/
  @Configuration
  protected static class WebMvcAutoconfig implements InitializingBean {

    @Autowired
    private SpringTemplateEngine springtemplateEngine;

    @Override
    public void afterPropertiesSet() throws Exception {
      ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
      resolver.setPrefix("META-INF/static");
      resolver.setSuffix(".html");
      resolver.setTemplateMode("HTML5");
      resolver.setCharacterEncoding("UTF-8");
      resolver.setCacheable(false);
      springtemplateEngine.addTemplateResolver(resolver);
    }


  };


}
