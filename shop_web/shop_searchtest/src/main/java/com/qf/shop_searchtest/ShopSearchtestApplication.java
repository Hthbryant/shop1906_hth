package com.qf.shop_searchtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = "com.qf")
@EnableEurekaClient
public class ShopSearchtestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopSearchtestApplication.class, args);
    }

}
