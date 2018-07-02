package com.uvanix.cloud.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author uvanix
 * @title 服务接口 - print
 * @date 2017/12/26
 */
@RestController
public class PrintController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Registration registration;

    public PrintController(Registration registration) {
        this.registration = registration;
    }


    @GetMapping("/print")
    public String print(@RequestParam String message) {
        logger.info("/print, host: {}, port: {}, service_id: {}, result: {}",
                registration.getHost(), registration.getPort(), registration.getServiceId(), message);

        return message;
    }
}
