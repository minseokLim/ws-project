package com.wsproject.wsservice.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * header에 있는 토큰 정보를 저장하기 위한 필터
 * @author mslim
 *
 */
@Component
public class AccessTokenFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		AccessTokenHolder.setAuthorization(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION));
		
		chain.doFilter(httpServletRequest, response);
	}

}
