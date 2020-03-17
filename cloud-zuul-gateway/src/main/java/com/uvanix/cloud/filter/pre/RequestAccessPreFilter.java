package com.uvanix.cloud.filter.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.uvanix.cloud.filter.BaseFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author
 * @date 2020/1/17
 */
@Slf4j
@Component
public class RequestAccessPreFilter extends ZuulFilter implements BaseFilter {

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 7;
	}

	@Override
	public boolean shouldFilter() {
		return shouldDoFilter();
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		shouldDoFilter(ctx, true);

		// 简单鉴权 可自定义鉴权方式
		HttpServletRequest request = ctx.getRequest();
		String token = request.getParameter(HttpHeaders.AUTHORIZATION);
		if (StringUtils.isBlank(token)) {
			log.warn("access token is blank");
			accessDeny(ctx, "access token is blank");
		}

		return null;
	}
}
