package com.wsproject.authsvr.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SessionInvalidateFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		String uri = req.getRequestURI();
		
		if(uri.contains("/oauth/authorize")) {
			HttpSession session = req.getSession();
			session.invalidate();
			
			Cookie[] cookies = req.getCookies();
			
			if(cookies != null) {
				for(Cookie cookie : cookies) {
					cookie.setMaxAge(0);
					res.addCookie(cookie);
					
					log.debug("Cookie : {}={}", cookie.getName(), cookie.getValue());
				}
			}
		} 
		
		chain.doFilter(request, response);
	}

}
