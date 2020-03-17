package com.uvanix.cloud.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Predicate;

/**
 * @author unknown
 * @date 2019/11/9
 */
@Slf4j
@UtilityClass
public class WebUtils extends org.springframework.web.util.WebUtils {

    private static final String[] IP_HEADER_NAMES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };
    private static final Predicate<String> IP_PREDICATE = (ip) -> !StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip);

    /**
     * 获取ip
     *
     * @param request
     * @return
     */
    @Nullable
    public static String getIp(@Nullable HttpServletRequest request) {
        if (request == null) {
            return "";
        }
        String ip = null;
        for (String ipHeader : IP_HEADER_NAMES) {
            ip = request.getHeader(ipHeader);
            if (!IP_PREDICATE.test(ip)) {
                break;
            }
        }
        if (IP_PREDICATE.test(ip)) {
            ip = request.getRemoteAddr();
        }
        return !StringUtils.hasText(ip) ? null :
                StringUtils.delimitedListToStringArray(ip, ",", " \t\n\n\f")[0];
    }
}
