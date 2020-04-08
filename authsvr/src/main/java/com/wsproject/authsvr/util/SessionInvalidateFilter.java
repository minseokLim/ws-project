package com.wsproject.authsvr.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SessionInvalidateFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		String uri = req.getRequestURI();
		
		if(uri.contains("/oauth/authorize")) {
			SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
			log.debug("authenticatioin is invalidated");
		} 
		
		chain.doFilter(request, response);
	}

}
