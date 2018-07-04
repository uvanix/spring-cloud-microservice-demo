package com.uvanix.cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author uvanix
 * @title 程序入口 - 配置客户端
 * @date 2018/1/9
 */
@SpringBootApplication
@EnableEurekaClient
@RefreshScope
public class ConfigClient {

    private final Logger logger = LoggerFactory.getLogger(ConfigClient.class);
    @Value("${test.intValue}")
    private int intValue;
    @Value("${test.strValue}")
    private String strValue;

    public static void main(String[] args) {
        SpringApplication.run(ConfigClient.class, args);
    }

    @Autowired
    void setEnvironment(Environment env) {
        logger.debug("word from env: {}", env.getProperty("word"));
    }

    /**
     * 此处使用/refresh端点刷新后会重载上下文不在执行调度
     */
    @Scheduled(fixedRate = 1000)
    public void test() {
        logger.debug("test.intValue -> {}", intValue);
        logger.debug("test.strValue -> {}", strValue);
    }
}
