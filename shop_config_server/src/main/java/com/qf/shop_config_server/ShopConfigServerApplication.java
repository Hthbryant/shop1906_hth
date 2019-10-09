package com.qf.shop_config_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ShopConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopConfigServerApplication.class, args);
    }

}
