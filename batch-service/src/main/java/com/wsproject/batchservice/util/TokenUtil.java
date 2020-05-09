package com.wsproject.batchservice.util;

import javax.annotation.PostConstruct;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.wsproject.batchservice.config.CustomProperties;
import com.wsproject.batchservice.dto.TokenInfo;
import com.wsproject.batchservice.util.RestUtil.RestUtilBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** 
 * 토큰 발급/재발급을 관리하는 유틸
 * @author mslim
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenUtil {
	
	private final CustomProperties properties;
	
	private RestUtilBuilder restUtilBuilder;
	
	@PostConstruct
	private void init() {
		String credentials = properties.getClientId() + ":" + properties.getClientSecret();
		String authorization = "Basic " + new String(Base64.encodeBase64(credentials.getBytes()));
		
		String url = properties.getApiPrivateBaseUri() + "/authsvr/oauth/token";
		
		restUtilBuilder = RestUtil.builder().url(url).post().headers(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
															.headers(HttpHeaders.AUTHORIZATION, authorization)
															.contentType(MediaType.APPLICATION_FORM_URLENCODED);
	}
	
	/** 
	 * 인증서버에 토큰 정보 요청
	 * @return 토큰정보
	 */
	public TokenInfo getTokenInfo() {
		log.info("getTokenInfo started");
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		
		params.add("grant_type", "password");
		params.add("scope", "mobile");
		params.add("username", "1");
		params.add("password", properties.getAdminPassword());
		
		TokenInfo tokenInfo = restUtilBuilder.bodyParam(params).build().exchange(TokenInfo.class).getBody();
		
		log.info("getTokenInfo ended");
		return tokenInfo;
	}
}
