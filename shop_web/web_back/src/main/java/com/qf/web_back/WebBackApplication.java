package com.qf.web_back;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "com.qf")
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.qf.feign")
@MapperScan(basePackages = "com.qf.dao")
@Import(FdfsClientConfig.class)
public class WebBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebBackApplication.class, args);
    }

}
