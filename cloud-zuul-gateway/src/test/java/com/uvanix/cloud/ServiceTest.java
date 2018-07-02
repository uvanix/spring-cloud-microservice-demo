package com.uvanix.cloud;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class ServiceTest extends ZuulGatewayTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void print() {
        ResponseEntity<String> entity = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/api/aaa/print?message=uvanix&token=1",
                String.class
        );
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        logger.info("\nresult: {}", entity.getBody());
    }

    @Test
    public void hello() {
        ResponseEntity<String> entity = testRestTemplate.getForEntity(
                "/api/bbb/hello?message=uvanix&token=1",
                String.class
        );
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        logger.info("\nresult: {}", entity.getBody());
    }
}