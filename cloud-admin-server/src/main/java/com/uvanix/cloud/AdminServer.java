package com.uvanix.cloud;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author uvanix
 * @title 程序入口 - Admin Server
 * @date 2018/4/8
 */
@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
public class AdminServer {

    public static void main(String[] args) {
        SpringApplication.run(AdminServer.class, args);
    }
}
