package com.uvanix.cloud.filter;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.uvanix.cloud.util.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 请求限流过滤器
 * 针对ip进行简单限流
 *
 * @author uvanix
 * @date 2018/9/29
 */
@Component
public class RequestLimitRatePreFilter extends ZuulFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final boolean limitRateEnabled;
    private final long limitRateNumber;
    private final LoadingCache<String, AtomicLong> ipCountCache;

    public RequestLimitRatePreFilter(@Value("${request.limit.rate.enabled:true}") boolean limitRateEnabled,
                                     @Value("${request.limit.rate.number:10}") long limitRateNumber) {
        this.limitRateEnabled = limitRateEnabled;
        this.limitRateNumber = limitRateNumber;
        this.ipCountCache = Caffeine.newBuilder().recordStats()
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .maximumSize(100000)
                .build(s -> new AtomicLong(0));
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 6;
    }

    @Override
    public boolean shouldFilter() {
        return limitRateEnabled;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set("shouldFilter", true);

        HttpServletRequest request = ctx.getRequest();
        String ip = IpUtils.getIpAddress(request);
        if (Objects.requireNonNull(ipCountCache.get(ip)).incrementAndGet() > limitRateNumber) {
            logger.warn("ip:{} 1s内限流{}, {}", ip, limitRateNumber, ipCountCache.stats());

            ctx.set("shouldFilter", false);
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.FORBIDDEN.value());

            HttpServletResponse response = ctx.getResponse();
            response.setCharacterEncoding("utf-8");
            ctx.setResponse(response);
            ctx.setResponseBody("请求过于频繁，请稍后再试！");
        }

        return null;
    }
}
