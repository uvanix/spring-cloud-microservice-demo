package com.uvanix.cloud.filter.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.uvanix.cloud.filter.BaseFilter;
import com.uvanix.cloud.util.WebUtils;
import io.github.bucket4j.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author
 * @date 2020/1/17
 */
@Slf4j
@Component
public class RequestLimitRatePreFilter extends ZuulFilter implements BaseFilter {

    private final Map<String, Bucket> ipBucketMap = new ConcurrentHashMap<>(128);
    private final boolean enable;
    private final int rate;
    private final int capacity;

    public RequestLimitRatePreFilter(@Value("${zuul.limitrate.enable:false}") boolean enable,
                                     @Value("${zuul.limitrate.rate:10}") int rate,
                                     @Value("${zuul.limitrate.capacity:20}") int capacity) {
        this.enable = enable;
        this.rate = rate;
        this.capacity = capacity;
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
        return enable;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        shouldDoFilter(ctx, true);

        HttpServletRequest request = ctx.getRequest();
        String ip = WebUtils.getIp(request);
        String uri = request.getRequestURI();

        Bucket bucket = ipBucketMap.computeIfAbsent(ip, k -> {
            Refill refill = Refill.greedy(rate, Duration.ofSeconds(1));
            Bandwidth limit = Bandwidth.classic(capacity, refill);
            return Bucket4j.builder().addLimit(limit).build();
        });

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (!probe.isConsumed()) {
            log.warn("IpAddress limit is exceeded: {} {}", ip, uri);
            accessDeny(ctx, "IpAddress limit is exceeded");
        }

        return null;
    }
}
