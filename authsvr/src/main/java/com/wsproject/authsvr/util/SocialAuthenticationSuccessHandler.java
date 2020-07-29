package com.wsproject.authsvr.util;

import static com.wsproject.authsvr.util.CommonUtil.getClientIP;
import static java.time.LocalDateTime.now;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.wsproject.authsvr.domain.AccessLog;
import com.wsproject.authsvr.domain.User;
import com.wsproject.authsvr.domain.enums.SocialType;
import com.wsproject.authsvr.service.AccessLogService;
import com.wsproject.authsvr.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * OAuth2로그인이 성공했을 때 실행되는 메소드를 관리하는 class
 * @author mslim
 *
 */
@Slf4j
@Component
@AllArgsConstructor
public class SocialAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	private UserService userService;
	
	private AccessLogService accessLogService;
	
	/**
	 * OAuth2로그인이 성공했을 때 실행되는 메소드 <br>
	 * 처음 로그인을 하는 경우, 사용자 정보를 DB에 저장한다. <br>
	 * 각기 다른 Social service로부터 로그인한 사용자들에 대해 <br>
	 * 인증서버에서 관리하는 key값을 바탕으로 생성된 토큰을 세션에 저장한다.
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		log.info("onAuthenticationSuccess started");
		
		OAuth2AuthenticationToken oAuth2Authentication = (OAuth2AuthenticationToken) authentication;
		
		SocialType socialType = SocialType.ofRegistrationId(oAuth2Authentication.getAuthorizedClientRegistrationId());
		Map<String, Object> userAttr = oAuth2Authentication.getPrincipal().getAttributes();
        User convertedUser = socialType.convertUser(userAttr);
        
        String principal = convertedUser.getPrincipal();
        Optional<User> foundUser = userService.selectUserByPrincipalAndSocialType(principal, socialType);
        
        User user;
        
        if(foundUser.isPresent()) {
        	user = foundUser.get();
        } else {
        	user = userService.insertUser(convertedUser);
        }
        
        Long userIdx = user.getIdx();
        // 인증서버에서 관리하는 key값(userIdx)을 기반으로 Token을 생성하여 저장
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userIdx, "N/A", user.getAuthorities()));
        
		// 액세스 로그 저장
		AccessLog accessLog = AccessLog.builder().userIdx(userIdx).ip(getClientIP(request)).accessDate(now()).build();
        accessLogService.insertAccessLog(accessLog);
        
        super.onAuthenticationSuccess(request, response, authentication);
		
		log.info("onAuthenticationSuccess ended");
	}
}
