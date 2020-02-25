package com.wsproject.zuulsvr.filter;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.wsproject.zuulsvr.constant.FilterType;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PreFilter extends ZuulFilter {
	private static final int FILTER_ORDER = 1;
	private static final boolean SHOULD_FILTER = true;
	private static final String FILTER_TYPE = FilterType.PRE_FILTER_TYPE;
	
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
		HttpServletRequest request = ctx.getRequest();
		
		log.info("Incoming request for {}", request.getRequestURI());
		
		Enumeration<String> keys = request.getParameterNames();
		
		// 파라미터가 1개 이상일 때
		if(keys.hasMoreElements()) {
			log.debug("===== Parameters start =====");
			
			while(keys.hasMoreElements()) {
				String key = keys.nextElement();
				log.debug(key + "=" + request.getParameter(key));
			}
			
			log.debug("===== Parameters end =====");
		}
		
		return null;
	}
}
