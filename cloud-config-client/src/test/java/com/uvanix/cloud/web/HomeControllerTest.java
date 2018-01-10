package com.uvanix.cloud.web;

import com.uvanix.cloud.ConfigClientTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;

public class HomeControllerTest extends ConfigClientTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void home() {
        ResponseEntity<String> entity = testRestTemplate.getForEntity("/", String.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        logger.info("\nresult: {}", entity.getBody());
    }

    @Test
    public void refresh() {
        ResponseEntity<String> entity = testRestTemplate.postForEntity("/refresh", null, String.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        logger.info("\nresult: {}", entity.getBody());
    }
}