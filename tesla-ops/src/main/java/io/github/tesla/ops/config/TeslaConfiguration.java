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
package io.github.tesla.ops.config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.google.common.collect.Lists;

/**
 * @author liushiming
 * @version TeslaAutoConfiguration.java, v 0.0.1 2018年5月7日 下午2:41:17 liushiming
 */
@Configuration
public class TeslaConfiguration {

  @Configuration
  @AutoConfigureAfter(FlywayAutoConfiguration.class)
  protected class FlywayConfigure {

    @Autowired
    private Flyway flyway;

    @PostConstruct
    public void init() {
      List<String> locations =
          new ArrayList<String>(Collections.singletonList("META-INF/config/sql"));
      flyway.setLocations(locations.toArray(new String[0]));
      flyway.setPlaceholderReplacement(false);
    }
  }

  @Configuration
  @EnableCaching
  protected class EhCacheConfigure {
    @Bean
    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean bean) {
      return new EhCacheCacheManager(bean.getObject());
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
      EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
      cacheManagerFactoryBean
          .setConfigLocation(new ClassPathResource("META-INF/config/ehcache.xml"));
      cacheManagerFactoryBean.setShared(true);
      return cacheManagerFactoryBean;
    }
  }

  @Configuration
  protected class DruidDataSourceConfigure {
    private Logger logger = LoggerFactory.getLogger(DruidDataSourceConfigure.class);
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Value("${spring.datasource.filters}")
    private String filters;

    @Value("{spring.datasource.connectionProperties}")
    private String connectionProperties;

    @Bean(initMethod = "init", destroyMethod = "close") // 声明其为Bean实例
    @Primary // 在同样的DataSource中，首先使用被标注的DataSource
    public DataSource dataSource() {
      DruidDataSource datasource = new DruidDataSource();

      datasource.setUrl(this.dbUrl);
      datasource.setUsername(username);
      datasource.setPassword(password);
      datasource.setDriverClassName(driverClassName);

      // configuration
      datasource.setInitialSize(initialSize);
      datasource.setMinIdle(minIdle);
      datasource.setMaxActive(maxActive);
      datasource.setMaxWait(maxWait);
      datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
      datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
      datasource.setValidationQuery(validationQuery);
      datasource.setTestWhileIdle(testWhileIdle);
      datasource.setTestOnBorrow(testOnBorrow);
      datasource.setTestOnReturn(testOnReturn);
      datasource.setPoolPreparedStatements(poolPreparedStatements);
      datasource
          .setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
      datasource.setProxyFilters(getProxyFilters());
      try {
        datasource.setFilters(filters);
      } catch (SQLException e) {
        logger.error("druid configuration initialization filter", e);
      }
      datasource.setConnectionProperties(connectionProperties);

      return datasource;
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
      ServletRegistrationBean reg = new ServletRegistrationBean();
      reg.setServlet(new StatViewServlet());
      reg.addUrlMappings("/druid/*");
      reg.addInitParameter("allow", ""); // 白名单
      return reg;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
      FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
      filterRegistrationBean.setFilter(new WebStatFilter());
      filterRegistrationBean.addUrlPatterns("/*");
      filterRegistrationBean.addInitParameter("exclusions",
          "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
      filterRegistrationBean.addInitParameter("profileEnable", "true");
      filterRegistrationBean.addInitParameter("principalCookieName", "USER_COOKIE");
      filterRegistrationBean.addInitParameter("principalSessionName", "USER_SESSION");
      filterRegistrationBean.addInitParameter("DruidWebStatFilter", "/*");
      return filterRegistrationBean;
    }


    private List<Filter> getProxyFilters() {
      List<Filter> proxyFilters = Lists.newArrayList();
      WallConfig wallConfig = new WallConfig();
      wallConfig.setDir("");
      wallConfig.setCommentAllow(true);
      wallConfig.init();
      WallFilter wallFilter = new WallFilter();
      wallFilter.setDbType("mysql");
      wallFilter.setConfig(wallConfig);
      proxyFilters.add(wallFilter);
      return proxyFilters;
    }
  }
}
