package com.uvanix.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author uvanix
 * @title 程序入口 - config server
 * @date 2018/1/8
 */
@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
public class ConfigServer {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServer.class, args);
    }
}
