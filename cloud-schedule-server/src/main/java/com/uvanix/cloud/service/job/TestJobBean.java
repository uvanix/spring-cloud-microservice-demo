package com.uvanix.cloud.service.job;

import com.uvanix.cloud.service.biz.LoggerService;
import org.springframework.stereotype.Component;

/**
 * @author uvanix
 * @date 2018/7/8
 */
@Component
public class TestJobBean {

    private final LoggerService loggerService;

    public TestJobBean(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    public void test() {
        loggerService.logger("testJob");
    }
}
