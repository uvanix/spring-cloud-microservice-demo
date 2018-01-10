package com.uvanix.cloud.web;

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
public class HomeController {

    @Value("${word}")
    private String word;

    @RequestMapping("/")
    @RefreshScope
    public String home() {
        return String.format("Hello %s !", word);
    }
}
