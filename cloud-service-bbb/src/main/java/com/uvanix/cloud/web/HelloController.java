package com.uvanix.cloud.web;

import com.uvanix.cloud.client.CloudServiceAaaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private Registration registration;
    @Autowired
    private CloudServiceAaaClient cloudServiceAaaClient;

    @GetMapping("/hello")
    public String hello(@RequestParam String message) {
        String result = cloudServiceAaaClient.print(message);
        logger.info("/hello, host: {}, port: {}, service_id: {}, result: {}",
                registration.getHost(), registration.getPort(), registration.getServiceId(), result);

        return String.format("Hello %s !", result);
    }
}
