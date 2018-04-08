package com.uvanix.cloud;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiGatewayTest {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Ignore
    @Test
    public void contextLoads() {
        logger.info("contextLoads method exec...");
    }
}
