package com.wsproject.authsvr.handler;

import static com.wsproject.authsvr.domain.enums.SocialType.FACEBOOK;
import static com.wsproject.authsvr.domain.enums.SocialType.GOOGLE;
import static com.wsproject.authsvr.domain.enums.SocialType.KAKAO;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.wsproject.authsvr.domain.User;
import com.wsproject.authsvr.domain.enums.RoleType;
import com.wsproject.authsvr.property.CustomProperties;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SocialAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	private CustomProperties properties;
	
	private RestTemplate restTemplate;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
	
		OAuth2AuthenticationToken oAuth2Authentication = (OAuth2AuthenticationToken) authentication;
		Map<String, Object> map = oAuth2Authentication.getPrincipal().getAttributes();
        User convertedUser = convertUser(oAuth2Authentication.getAuthorizedClientRegistrationId(), map);
        User user = restTemplate.postForObject(properties.getApiBaseUri() + "/user-service/v1.0/users", convertedUser, User.class);
    	
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getIdx(), "N/A", user.getAuthorities()));
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	private User convertUser(String registrationId, Map<String, Object> map) {
        if(FACEBOOK.getValue().equals(registrationId)) {
        	return getFacebookUser(map);
        } else if(GOOGLE.getValue().equals(registrationId)) {
        	return getGoogleUser(map);
        } else if(KAKAO.getValue().equals(registrationId)) {
        	return getKaKaoUser(map);
        }
        
        return null;
    }
	
	private User getFacebookUser(Map<String, Object> map) {
    	@SuppressWarnings("unchecked")
		Object pictureUrl = ((HashMap<String, Object>) ((HashMap<String, Object>) map.get("picture")).get("data")).get("url");
    	
    	return User.builder()
                .name(convertObjToStr(map.get("name")))
                .email(convertObjToStr(map.get("email")))
                .principal(convertObjToStr(map.get("id")))
                .socialType(FACEBOOK)
                .pictureUrl(convertObjToStr(pictureUrl))
                .roles(Collections.singletonList(RoleType.USER.getValue()))
                .build();
    }

    private User getGoogleUser(Map<String, Object> map) {
    	return User.builder()
                .name(convertObjToStr(map.get("name")))
                .email(convertObjToStr(map.get("email")))
                .principal(convertObjToStr(map.get(IdTokenClaimNames.SUB)))
                .socialType(GOOGLE)
                .pictureUrl(convertObjToStr(map.get("picture")))
                .roles(Collections.singletonList(RoleType.USER.getValue()))
                .build();
    }
    
    private User getKaKaoUser(Map<String, Object> map) {
        @SuppressWarnings("unchecked")
		Map<String, String> propertyMap = (HashMap<String, String>) map.get("properties");
        
        return User.builder()
                .name(propertyMap.get("nickname"))
                .email(convertObjToStr(map.get("kaccount_email")))
                .principal(convertObjToStr(map.get("id")))
                .socialType(KAKAO)
                .pictureUrl(propertyMap.get("thumbnail_image"))
                .roles(Collections.singletonList(RoleType.USER.getValue()))
                .build();
    }
    
    private String convertObjToStr(Object obj) {
    	return obj != null ? String.valueOf(obj) : null;
    }
}