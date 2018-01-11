package com.uvanix.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @author uvanix
 * @title 程序入口-微服务bbb
 * @date 2017/12/26
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceBbb {

    public static void main(String[] args) {
        SpringApplication.run(ServiceBbb.class, args);
    }
}
