package com.uvanix.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import zipkin.server.EnableZipkinServer;

/**
 * @author uvans
 * @title 程序入口 - Zipkin Server
 * @date 2018/4/8
 */
@SpringBootApplication
@EnableEurekaClient
@EnableZipkinServer
public class ZipkinServer {

    public static void main(String[] args) {
        SpringApplication.run(ZipkinServer.class, args);
    }
}
