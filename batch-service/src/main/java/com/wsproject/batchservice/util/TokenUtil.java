package com.wsproject.batchservice.util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.wsproject.batchservice.dto.TokenInfo;
import com.wsproject.batchservice.property.CustomProperties;

import lombok.RequiredArgsConstructor;

/** 토큰 발급/재발급을 관리하는 유틸
 * @author mslim
 *
 */
@Component
@RequiredArgsConstructor
public class TokenUtil {
	
	private final Gson gson;
	
	private final CustomProperties properties;
	
	private final RestTemplate restTemplate;
	
	/** 인증서버에 토큰 정보 요청
	 * @return 토큰정보
	 */
	public TokenInfo getTokenInfo() {		
		String credentials = properties.getClientId() + ":" + properties.getClientSecret();
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.add(HttpHeaders.AUTHORIZATION, "Basic " + encodedCredentials);
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		
		params.add("grant_type", "password");
		params.add("scope", "admin");
		params.add("username", "1");
		params.add("password", properties.getAdminPassword());
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
		
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(properties.getApiBaseUri() + "/authsvr/oauth/token", entity, String.class);
		
		return gson.fromJson(responseEntity.getBody(), TokenInfo.class);
	}
}
