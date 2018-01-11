package com.uvanix.cloud.client;

import com.uvanix.cloud.ServiceBbbTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class CloudServiceAaaClientTest extends ServiceBbbTest {

    @Autowired
    private CloudServiceAaaClient cloudServiceAaaClient;

    @Test
    public void print() {
        String result = cloudServiceAaaClient.print("uvanix");
        assertNotNull(result);
        logger.info("\nresult: {}", result);
    }
}