package com.uvanix.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author uvanix
 * @title 程序入口 - Service Task
 * @date 2018/4/8
 */
@SpringBootApplication
@EnableEurekaClient
public class ServiceTask {

    public static void main(String[] args) {
        SpringApplication.run(ServiceTask.class, args);
    }
}
