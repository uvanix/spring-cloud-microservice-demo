package com.uvanix.cloud.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author uvanix
 * @title 微服务客户端 - cloud-service-aaa
 * @date 2017/12/26
 */
@FeignClient("cloud-service-aaa")
public interface CloudServiceAaaClient {

    /**
     * 调用cloud-service-aaa提供的服务print
     * 注意此处RequestParam注解需填写value否则会抛出异常：
     * nested exception is java.lang.IllegalStateException: RequestParam.value() was empty on parameter 0
     *
     * @param message
     * @return
     */
    @GetMapping("/print")
    String print(@RequestParam("message") String message);
}
