package com.uvanix.cloud.service.util;

import java.util.UUID;

/**
 * @author uvanix
 * @date 2018/7/6
 */
public final class UuidUtils {

    private UuidUtils() {
    }

    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
