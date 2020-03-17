package com.uvanix.cloud.filter.post;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.uvanix.cloud.filter.BaseFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Base64;

/**
 * @author
 * @date 2020/1/19
 */
@Slf4j
@Component
public class ResponseEncryptPostFilter extends ZuulFilter implements BaseFilter {

    private final boolean enable;

    public ResponseEncryptPostFilter(@Value("${zuul.limitrate.enable:false}") boolean enable) {
        this.enable = enable;
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
        return enable && shouldDoFilter();
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        shouldDoFilter(ctx, true);

        HttpServletRequest request = ctx.getRequest();
        String uri = request.getRequestURI();
        try {
            if (uri.startsWith("/api")) {
                HttpServletResponse response = ctx.getResponse();
                response.setHeader(X_SERVER_ENCRYPT, "1");

                InputStream is = ctx.getResponseDataStream();
                byte[] body = StreamUtils.copyToByteArray(is);

                // 简单base64 可自定义加密方式(如RSA签名key配AES加密)
                String encrypt = Base64.getEncoder().encodeToString(body);
                ctx.setResponseBody(encrypt);
            }
        } catch (Exception e) {
            log.error("{} encrypt error! {}", uri, e.getMessage(), e);
            accessDeny(ctx, "encrypt error!");
        }

        return null;
    }
}
