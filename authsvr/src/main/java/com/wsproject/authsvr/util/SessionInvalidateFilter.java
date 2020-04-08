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

@Component
public class SessionInvalidateFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		// 모든 Filterchain이 끝난 후에 로직을 실행
		chain.doFilter(request, response);
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		HttpSession session = req.getSession();
		session.invalidate();
		
		Cookie[] cookies = req.getCookies();
		
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				cookie.setMaxAge(0);
				res.addCookie(cookie);
			}
		}
	}
}
