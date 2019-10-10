package com.qf.web_back;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.qf")
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.qf.feign")
@MapperScan(basePackages = "com.qf.dao")
public class WebBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebBackApplication.class, args);
    }

}
