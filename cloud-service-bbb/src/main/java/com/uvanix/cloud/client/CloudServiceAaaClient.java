package com.uvanix.cloud.client;

import feign.hystrix.FallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author uvanix
 * @title 微服务客户端 - cloud-service-aaa
 * @date 2017/12/26
 */
@FeignClient(value = "cloud-service-aaa", fallbackFactory = CloudServiceAaaClient.HystrixCloudServiceAaaClientFactory.class)
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

    @Component
    class HystrixCloudServiceAaaClientFactory implements FallbackFactory<CloudServiceAaaClient> {
        @Override
        public CloudServiceAaaClient create(Throwable throwable) {
            return message -> "fallback; reason was: " + throwable.getMessage();
        }
    }
}
