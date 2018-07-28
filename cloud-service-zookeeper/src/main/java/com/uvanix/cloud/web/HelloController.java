package com.uvanix.cloud.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author uvanix
 * @title 服务接口 - hello
 * @date 2017/12/26
 */
@RestController
public class HelloController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Registration registration;

    public HelloController(Registration registration) {
        this.registration = registration;
    }

    @GetMapping("/hello")
    public String hello(@RequestParam String message) {
        logger.info("/hello, host: {}, port: {}, service_id: {}, result: {}",
                registration.getHost(), registration.getPort(), registration.getServiceId(), message);

        return String.format("Hello %s !", message);
    }
}
