package com.uvanix.cloud;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ServiceBbbTest {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Ignore
    @Test
    public void contextLoads() {
        logger.info("contextLoads method exec...");
    }

    @Test
    public void health() {
        ResponseEntity<Map> entity = testRestTemplate
                .getForEntity("/actuator/health", Map.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        logger.info("\nresult: {}", entity.getBody());
    }
}