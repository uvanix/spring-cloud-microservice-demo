package com.uvanix.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
 * @author uvanix
 * @title 程序入口 - Schedule Server
 * @date 2018/4/8
 */
@SpringBootApplication
@EnableEurekaClient
@EnableScheduling
@EnableTransactionManagement
@MapperScan("com.uvanix.cloud.dao.mapper")
public class ScheduleServer {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleServer.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
