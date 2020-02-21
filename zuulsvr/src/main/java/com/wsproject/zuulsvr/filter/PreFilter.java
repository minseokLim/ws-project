package com.wsproject.zuulsvr.filter;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.wsproject.zuulsvr.util.FilterUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PreFilter extends ZuulFilter {
	private static final int FILTER_ORDER = 1;
	private static final boolean SHOULD_FILTER = true;
	private static final String FILTER_TYPE = FilterUtil.PRE_FILTER_TYPE;
	
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
		log.info("Processing incoming request for {}", ctx.getRequest().getRequestURI());
		return null;
	}
}
