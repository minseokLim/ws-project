package com.wsproject.batchservice.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import com.wsproject.batchservice.config.CustomProperties;
import com.wsproject.batchservice.dto.TokenInfo;

import lombok.extern.slf4j.Slf4j;

/** 
 * RestTemplate을 이용한 요청에 대한 중복코드를 최소화 하기위해 생성한 유틸
 * @author mslim
 *
 */
@Slf4j
public class RestUtil {
	
	private String url;
	private HttpMethod method;
	private HttpHeaders headers;
	private TokenInfo tokenInfo;
	private Map<String, List<String>> queryParams;
	private Object bodyParam;
	
	// 토큰 정보를 Header에 주입할 때 토큰 앞에 붙일 값
	private static final String BEARER_PREFIX = "Bearer ";
		
	public RestUtil(String url, HttpMethod method, HttpHeaders headers, TokenInfo tokenInfo, 
					Map<String, List<String>> queryParams, Object bodyParam) {
		this.url = Objects.requireNonNull(url);
		this.method = method;
		this.headers = headers;
		this.tokenInfo = Objects.requireNonNull(tokenInfo);
		this.queryParams = queryParams;
		this.bodyParam = bodyParam;
	}
	
	/**
	 * RestTemplate과 객체에 있는 정보를 기반으로 http 요청을 한다. <br>
	 * ResponseEntity에 담길 body의 class가 지정이 안 될 경우 String
	 * @return 리스판스
	 */
	public ResponseEntity<String> exchange() {
		return exchange(String.class);
	}
	
	/**
	 * RestTemplate과 객체에 있는 정보를 기반으로 http 요청을 한다.
	 * @param <T>
	 * @param clazz ResponseEntity에 담길 body의 class
	 * @return 리스판스
	 */
	public <T> ResponseEntity<T> exchange(Class<T> clazz) {
		log.debug("exchange started - url : {}", url);
		CustomProperties properties = CommonUtil.getBean(CustomProperties.class);
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(properties.getApiPrivateBaseUri() + url);
		queryParams.entrySet().forEach(entry -> builder.queryParam(entry.getKey(), entry.getValue()));
		
		if(tokenInfo != null) {
			headers.add(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + tokenInfo.getAccess_token()); // 토큰 정보를 헤더에 주입
		}
		
		HttpEntity<Object> entity = new HttpEntity<Object>(bodyParam, headers);
		
		RestOperations restTemplate = CommonUtil.getBean(RestOperations.class);
		
		ResponseEntity<T> result = restTemplate.exchange(builder.build().toUri(), method, entity, clazz);
		
		log.debug("exchange ended - url : {}" , url);
		return result;
	}
	
	public static RestUtilBuilder builder() {
		return new RestUtilBuilder();
	}
	
	public static class RestUtilBuilder {
		
		private String url;
		private HttpMethod method = HttpMethod.GET;
		private HttpHeaders headers = new HttpHeaders();
		private TokenInfo tokenInfo;
		private Map<String, List<String>> queryParams = new LinkedHashMap<>();
		private Object bodyParam;
		
		public RestUtilBuilder url(String url) {
			this.url = url;
			return this;
		}
		
		/** 
		 * HttpMethod를 GET으로 설정
		 * @return
		 */
		@Deprecated
		public RestUtilBuilder get() {
			method = HttpMethod.GET;
			return this;
		}
		
		/** 
		 * HttpMethod를 POST로 설정
		 * @return
		 */
		public RestUtilBuilder post() {
			method = HttpMethod.POST;
			return this;
		}
		
		/** 
		 * HttpMethod를 PUT으로 설정
		 * @return
		 */
		public RestUtilBuilder put() {
			method = HttpMethod.PUT;
			return this;
		}
		
		/** 
		 * HttpMethod를 DELETE로 설정
		 * @return
		 */
		public RestUtilBuilder delete() {
			method = HttpMethod.DELETE;
			return this;
		}
		
		/** 
		 * header에 key, value형태로 값을 추가
		 * @param headerName
		 * @param headerValue
		 * @return
		 */
		public RestUtilBuilder headers(String headerName, String headerValue) {
			headers.add(headerName, headerValue);
			return this;
		}
		
		/** 
		 * header에 contentType을 설정
		 * @param mediaType
		 * @return
		 */
		public RestUtilBuilder contentType(MediaType mediaType) {
			headers.setContentType(mediaType);
			return this;
		}
		
		/** 
		 * 요청에 쓰일 토큰정보 설정
		 * @param tokenInfo
		 * @return
		 */
		public RestUtilBuilder tokenInfo(TokenInfo tokenInfo) {
			this.tokenInfo = tokenInfo;
			return this;
		}
		
		/** 
		 * 쿼리파라미터를 key, value형태로 추가
		 * @param key
		 * @param value
		 * @return
		 */
		public RestUtilBuilder queryParam(String key, String value) {
			List<String> list = queryParams.get(key);
			
			if(list == null) {
				list = new ArrayList<String>();
				queryParams.put(key, list);
			}
			
			list.add(value);
			return this;
		}

		/**
		 * POST/PUT 전송 시 body로 보낼 파라미터를 지정
		 * @param bodyParam
		 * @return
		 */
		public RestUtilBuilder bodyParam(Object bodyParam) {
			this.bodyParam = bodyParam;
			return this;
		}
		
		public RestUtil build() {
			return new RestUtil(url, method, headers, tokenInfo, queryParams, bodyParam);
		}
	}
}
