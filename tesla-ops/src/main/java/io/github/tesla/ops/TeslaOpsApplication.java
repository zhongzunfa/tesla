package io.github.tesla.ops;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@ComponentScan(basePackages = {"io.github.tesla.ops", "io.github.tesla.authz"})
@MapperScan(basePackages = {"io.github.tesla.common", "io.github.tesla.ops"},
    annotationClass = Mapper.class)
@SpringBootApplication
public class TeslaOpsApplication {
  public static void main(String[] args) {
    SpringApplication.run(TeslaOpsApplication.class, args);
  }

}
