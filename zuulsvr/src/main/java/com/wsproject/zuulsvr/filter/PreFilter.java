package com.wsproject.zuulsvr.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.wsproject.zuulsvr.util.FilterUtil;

@Component
public class PreFilter extends ZuulFilter {
	private static final int FILTER_ORDER = 1;
	private static final boolean SHOULD_FILTER = true;
	private static final String FILTER_TYPE = FilterUtil.PRE_FILTER_TYPE;
	private static final Logger logger = LoggerFactory.getLogger(PreFilter.class);
	
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
		logger.debug("Processing incoming request for {}", ctx.getRequest().getRequestURI());
		return null;
	}
}
