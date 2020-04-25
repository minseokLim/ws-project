package com.wsproject.batchservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * application.yml에 custom으로 추가한 속성을 불러오는 class
 * @author mslim
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {
	String apiPrivateBaseUri; // docker 내부에서 사용하는 zuul server의 base uri
	String clientId; // 인증서버에 토큰을 요청할 때 쓰는 Client ID
	String clientSecret; // 인증서버에 토큰을 요청할 때 쓰는 Client Secret
	String adminPassword; // 인증서버에 PASSWORD 방식으로 토큰을 요청할 때 쓰이는 password
						  // userIdx가 1인 사용자에게 ADMIN권한을 주고 이 패스워드를 부여
}
