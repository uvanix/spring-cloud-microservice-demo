package com.uvanix.cloud;

import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @author uvanix
 * @title 程序入口 - 微服务bbb
 * @date 2017/12/26
 */
@SpringCloudApplication
@EnableFeignClients
public class ServiceBbb {

    public static void main(String[] args) {
        SpringApplication.run(ServiceBbb.class, args);
    }

    /**
     * feign请求日志全局配置
     *
     * @return
     */
    @Bean
    public feign.Logger.Level feignLoggerLevel() {
        return Logger.Level.NONE;
    }
}
