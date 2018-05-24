package io.github.tesla.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class TeslaSampleSpringCloud {
  public static void main(String[] args) {
    SpringApplication.run(TeslaSampleSpringCloud.class, args);
  }

}
