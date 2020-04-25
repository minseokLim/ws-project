package com.wsproject.authsvr.config;

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
	String jwtSignkey; // jwt token 생성에 사용되는 sign key
}
