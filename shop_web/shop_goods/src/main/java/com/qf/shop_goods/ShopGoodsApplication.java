package com.qf.shop_goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.qf")
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.qf.feign")
@MapperScan(basePackages = "com.qf.dao")
public class ShopGoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopGoodsApplication.class, args);
    }

}
