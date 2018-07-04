package com.uvanix.cloud.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author uvanix
 * @title
 * @date 2018/1/9
 */
@RestController
@RefreshScope
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Value("${word:uvanix}")
    private String word;
    @Value("${test.intValue}")
    private int intValue;
    @Value("${test.strValue}")
    private String strValue;

    @RequestMapping("/")
    public String home() {
        logger.debug("test.intValue -> {}", intValue);
        logger.debug("test.strValue -> {}", strValue);

        logger.debug("Hello {} !", word);
        return String.format("Hello %s !", word);
    }
}
