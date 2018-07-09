package com.uvanix.cloud.service.biz.impl;

import com.uvanix.cloud.service.biz.LoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author uvanix
 * @date 2018/7/6
 */
@Service
public class LoggerServiceImpl implements LoggerService {

    private final Logger logger = LoggerFactory.getLogger(LoggerServiceImpl.class);

    @Override
    public void logger(String jobName) {
        logger.info("test quartz job -> {} {}", jobName, LocalDateTime.now());
    }
}
