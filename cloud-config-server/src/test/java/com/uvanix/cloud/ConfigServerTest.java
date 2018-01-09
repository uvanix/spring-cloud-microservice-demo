package com.uvanix.cloud;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ConfigServerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Value("${security.user.name}")
    private String username;
    @Value("${security.user.password}")
    private String password;

    @Test
    public void configurationAvailable() {
        ResponseEntity<String> entity = testRestTemplate.withBasicAuth(username, password)
                .getForEntity("/config-default.yml", String.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }
}
