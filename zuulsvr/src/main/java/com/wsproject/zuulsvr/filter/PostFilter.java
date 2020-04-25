package com.wsproject.zuulsvr.filter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.wsproject.zuulsvr.constant.FilterType;

import brave.Tracer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 사후 필터 <br>
 * 클라이언트로 response를 반환할 때 실행될 필터
 * @author mslim
 *
 */
@Component
@AllArgsConstructor
@Slf4j
public class PostFilter extends ZuulFilter {
	private static final int FILTER_ORDER = 1;
	private static final boolean SHOULD_FILTER = true;
	private static final String FILTER_TYPE = FilterType.POST_FILTER_TYPE;
	
	private Tracer tracer;
	
	@Override
	public int filterOrder() {
		return FILTER_ORDER;
	}
	
	@Override
	public boolean shouldFilter() {
		return SHOULD_FILTER;
	}
	
	@Override
	public String filterType() {
		return FILTER_TYPE;
	}
	
	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();

		
		HttpServletResponse response = ctx.getResponse();
		
		// 스프링 클라우드 슬루스에서 사용되는 상관관계 아이디를 전파한다.
		String traceId = tracer.currentSpan().context().traceIdString();
		response.addHeader("x-b3-traceid", traceId);
		
		log.info("Outgoing request for {}", ctx.getRequest().getRequestURI());
		return null;
	}
}
