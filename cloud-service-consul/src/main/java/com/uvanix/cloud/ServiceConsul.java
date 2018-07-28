package com.uvanix.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author uvanix
 * @date 2018/7/28
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServiceConsul {

    public static void main(String[] args) {
        SpringApplication.run(ServiceConsul.class, args);
    }
}
