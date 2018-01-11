package com.uvanix.cloud.web;

import com.uvanix.cloud.ServiceAaaTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;

public class PrintControllerTest extends ServiceAaaTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void print() {
        ResponseEntity<String> entity = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/print?message=uvanix",
                String.class
        );
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        logger.info("\nresult: {}", entity.getBody());
    }
}