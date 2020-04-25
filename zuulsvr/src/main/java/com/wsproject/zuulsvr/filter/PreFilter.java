package com.wsproject.zuulsvr.filter;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.wsproject.zuulsvr.constant.FilterType;

import lombok.extern.slf4j.Slf4j;

/**
 * 사전 필터 <br>
 * 클라이언트로 요청이 들어왔을 때 실행될 필터
 * @author mslim
 *
 */
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
		
		if(log.isDebugEnabled()) {
			Enumeration<String> keys = request.getParameterNames();
			List<String> paramStrs = new ArrayList<>();
			
			while(keys.hasMoreElements()) {
				String key = keys.nextElement();
				paramStrs.add(key + "=" + request.getParameter(key));
			}
			
			log.debug("Parameters : {}", String.join(", ", paramStrs));
		}
		
		return null;
	}
}
