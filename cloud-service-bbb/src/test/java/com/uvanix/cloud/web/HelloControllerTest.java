package com.uvanix.cloud.web;

import com.uvanix.cloud.ServiceBbbTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;

public class HelloControllerTest extends ServiceBbbTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void hello() {
        ResponseEntity<String> entity = testRestTemplate.getForEntity(
                "/hello?message=uvanix",
                String.class
        );
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        logger.info("\nresult: {}", entity.getBody());
    }
}