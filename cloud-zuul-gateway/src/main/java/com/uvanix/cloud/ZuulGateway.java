package com.uvanix.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

/**
 * @author uvanix
 * @title 程序入口 - API网关
 * @date 2018/4/7
 */
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@EnableFeignClients
public class ZuulGateway {

    public static void main(String[] args) {
        SpringApplication.run(ZuulGateway.class, args);
    }

    /**
     * feign请求日志全局配置
     *
     * @return
     */
    @Bean
    public feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }
}
