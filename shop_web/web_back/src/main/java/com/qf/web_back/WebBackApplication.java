package com.qf.web_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = "com.qf")
@EnableEurekaClient
public class WebBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebBackApplication.class, args);
    }

}
