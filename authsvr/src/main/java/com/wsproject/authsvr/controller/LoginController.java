package com.wsproject.authsvr.controller;

import java.util.Arrays;
import java.util.List;

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
		
	/**
	 * 로그인 페이지로 이동
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping(value = "/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		
		if(!isValidApproach(request, response)) {
			return "redirect:/invalidApproach";
		}
		
		return "login";
	}
	
	/**
	 * 유효하지 않은 접근일 때 이동하는 페이지
	 * @return
	 */
	@GetMapping(value = "/invalidApproach")
	public String invalidApproach() {
		return "invalidApproach";
	}
	
	/**
	 * 유효한 접근인지를 판단. <br>
	 * 클라이언트에서의 호출이 아닌, 인증서버 url을 직접 호출하거나 <br>
	 * 필요한 파라미터들이 없는 상태로 호출이 될 경우 false를 반환
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
			List<String> params = Arrays.asList("response_type", "client_id", "scope", "state", "redirect_uri"); // 넘어와야하는 파라미터
			result = uriComponents.getQueryParams().keySet().containsAll(params);
			
			log.debug("Query String {}", uriComponents.getQuery());
		}
		
		return result;
	}
}
