package com.wsproject.batchservice.service;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.wsproject.batchservice.dto.TokenInfo;
import com.wsproject.batchservice.property.CustomProperties;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class RestService {

	private RestTemplate restTemplate;
	
	private CustomProperties properties;
	
	private Gson gson;
	
	public ResponseEntity<String> getForEntity(String url, TokenInfo tokenInfo) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenInfo.getAccess_token());
		HttpEntity<Void> entity = new HttpEntity<>(headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		
		return responseEntity;
	}
	
	public ResponseEntity<String> postForEntity(String url, TokenInfo tokenInfo, Object object) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenInfo.getAccess_token());
		HttpEntity<Object> entity = new HttpEntity<Object>(object, headers);
		
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		
		return responseEntity;
	}
	
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
