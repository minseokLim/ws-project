package com.wsproject.authsvr.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class LoginController {
		
	@GetMapping(value = "/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		
		if(!isValidApproach(request, response)) {
			return "redirect:/invalidApproach";
		}
		
		Cookie[] cookies = request.getCookies();
		
		for(Cookie cookie : cookies) {
			log.debug("name : {}, value: {}, path: {}, domain: {}, maxAge: {}", cookie.getName(), cookie.getValue(), cookie.getPath(), cookie.getDomain(), cookie.getMaxAge());
		}
		return "login";
	}
	
	@GetMapping(value = "/invalidApproach")
	public String invalidApproach() {
		return "invalidApproach";
	}
	/**
	 * 유효한 접근인지를 판단. <br>
	 * 클라이언트에서의 호출이 아닌, 인증서버 url을 직접 호출하거나 필요한 파라미터들이 없는 상태로 호출이 될 경우 false를 반환
	 * @param request
	 * @param response
	 * @return 유효한 접근인지의 여부
	 */
	private boolean isValidApproach(HttpServletRequest request, HttpServletResponse response) {
		RequestCache requestCache = new HttpSessionRequestCache();
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		
		boolean result = false;
		
		if(savedRequest != null) {
			UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(savedRequest.getRedirectUrl()).build();
			Set<String> keySet = uriComponents.getQueryParams().keySet();
			List<String> params = Arrays.asList("response_type", "client_id", "scope", "state", "redirect_uri"); // 넘어와야하는 파라미터
			
			if(keySet.containsAll(params)) {
				result = true;
			}
			
			log.debug("Query String {}", uriComponents.getQuery());
		}
		
		return result;
	}
}
