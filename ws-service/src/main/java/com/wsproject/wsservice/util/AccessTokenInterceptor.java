package com.wsproject.wsservice.util;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * AccessTokenHolder에 저장되어 있던 토큰 정보를 request header에 넣는 인터셉터 <br>
 * 이를 통해 다른 서비스 호출 시 토큰 전파가 가능하다.
 * @author mslim
 *
 */
public class AccessTokenInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		HttpHeaders headers = request.getHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, AccessTokenHolder.getAuthorization());
				
		return execution.execute(request, body);
	}
}
