package com.jk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient // 开启eureka客户端
@EnableFeignClients //开启识别FeignClient的注解
public class CloudConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudConsumerApplication.class, args);
    }

}
