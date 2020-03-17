package com.uvanix.cloud.filter;

import com.netflix.zuul.context.RequestContext;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

/**
 * @author
 * @date 2020/1/17
 */
public interface BaseFilter {

	/**
	 * 每次请求服务端返回当前时间
	 */
	String X_SERVER_TIMESTAMP = "X-Server-TimeStamp";
	/**
	 * 响应结果是否加密
	 */
	String X_SERVER_ENCRYPT = "X-Server-Encrypt";

	/**
	 * 设置是否执行下一个filter
	 *
	 * @param ctx
	 * @param should
	 */
	default void shouldDoFilter(RequestContext ctx, boolean should) {
		HttpServletResponse response = ctx.getResponse();
		response.setHeader(X_SERVER_TIMESTAMP, System.currentTimeMillis() / 1000 + "");
		ctx.set("shouldDoFilter", should);
	}

	/**
	 * 判断是否执行下一个filter
	 *
	 * @return
	 */
	default boolean shouldDoFilter() {
		return RequestContext.getCurrentContext().getBoolean("shouldDoFilter");
	}

	/**
	 * 访问受限
	 *
	 * @param ctx
	 * @param body
	 */
	default void accessDeny(RequestContext ctx, String body) {
		shouldDoFilter(ctx, false);
		ctx.setSendZuulResponse(false);
		ctx.setResponseStatusCode(HttpStatus.OK.value());

		HttpServletResponse response = ctx.getResponse();
//		response.setHeader("Content-type", "application/json;charset=UTF-8");
//		response.setCharacterEncoding("utf-8");
		ctx.setResponse(response);
		ctx.setResponseBody(body);
	}
}
