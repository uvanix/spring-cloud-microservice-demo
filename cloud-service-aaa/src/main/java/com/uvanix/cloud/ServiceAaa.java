package com.uvanix.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author uvanix
 * @title 程序入口 - 服务aaa
 * @date 2017/12/26
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServiceAaa {

    public static void main(String[] args) {
        SpringApplication.run(ServiceAaa.class, args);
    }
}
