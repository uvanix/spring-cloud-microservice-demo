package com.uvanix.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.EnableZipkinServer;

/**
 * @author uvans
 * @title 程序入口 - Sleuth Zipkin
 * @date 2018/4/8
 */
@SpringBootApplication
@EnableZipkinServer
public class SletuhZipkin {

    public static void main(String[] args) {
        SpringApplication.run(SletuhZipkin.class, args);
    }
}
