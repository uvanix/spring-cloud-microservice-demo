package com.uvanix.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.core.env.Environment;

/**
 * @author uvanix
 * @title 程序入口 - 配置客户端
 * @date 2018/1/9
 */
@SpringBootApplication
@EnableEurekaClient
public class ConfigClient {

    public static void main(String[] args) {
        SpringApplication.run(ConfigClient.class, args);
    }

    @Autowired
    void setEnvironment(Environment env) {
        System.out.println("\nword from env: " + env.getProperty("word"));
    }
}
