package com.wsproject.authsvr.controller;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.wsproject.authsvr.dto.OAuthToken;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/oauth2")
public class AuthController {
	
	private final Gson gson;
	private final RestTemplate restTemplate;
	
	// TODO should be in client server. not here
	@GetMapping(value = "/callback")
	public OAuthToken callbackSocial(@RequestParam String code) {
		
		String credentials = "ws-project:zq8WAZ5V9GVQK6COD2TQSfvOzExRibD4";
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.add("Authorization", "Basic " + encodedCredentials);
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("code", code);
		params.add("grant_type", "authorization_code");
		params.add("redirect_uri", "http://localhost:8901/oauth2/callback");
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8901/oauth/token", request, String.class);
		
		if(response.getStatusCode() == HttpStatus.OK) {
			return gson.fromJson(response.getBody(), OAuthToken.class);
		}
		
		return null;
	}
	
	@GetMapping(value = "/token/refresh")
	public OAuthToken refreshToken(@RequestParam String refreshToken) {
		
		String credentials = "testClientId:testSecret";
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.add("Authorization", "Basic " + encodedCredentials);
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("refresh_token", refreshToken);
		params.add("grant_type", "refresh_token");
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8081/oauth/token", request, String.class);
		
		if(response.getStatusCode() == HttpStatus.OK) {
			return gson.fromJson(response.getBody(), OAuthToken.class);
		}
		
		return null;
	}
}
