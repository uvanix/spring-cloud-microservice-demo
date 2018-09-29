package com.uvanix.cloud.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Base64;

/**
 * 响应数据安全控制
 * 对响应结果进行加密
 *
 * @author uvanix
 * @date 2018/6/4
 */
@Component
public class ResponseSecurityPostFilter extends ZuulFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final boolean resEncryptEnabled;

    public ResponseSecurityPostFilter(@Value("${response.security.encrypt.enabled:true}") boolean resEncryptEnabled) {
        this.resEncryptEnabled = resEncryptEnabled;
    }

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 501;
    }

    @Override
    public boolean shouldFilter() {
        return resEncryptEnabled;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set("shouldFilter", true);

        HttpServletRequest request = ctx.getRequest();
        String uri = request.getRequestURI();
        try {
            if (uri.startsWith("/api")) {
                HttpServletResponse response = ctx.getResponse();
                response.addHeader("X-Security-Encrypt", Boolean.toString(resEncryptEnabled));

                InputStream out = ctx.getResponseDataStream();
                byte[] body = StreamUtils.copyToByteArray(out);

                // 简单base64 可自定义加密方式
                String encrypt = Base64.getEncoder().encodeToString(body);
                ctx.setResponseBody(encrypt);
            }
        } catch (Exception e) {
            logger.error("{} encrypt error! {}", uri, e.getMessage(), e);

            ctx.set("shouldFilter", false);
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

            HttpServletResponse response = ctx.getResponse();
            response.setCharacterEncoding("utf-8");
            ctx.setResponse(response);
            ctx.setResponseBody(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }

        return null;
    }
}
