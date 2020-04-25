package com.wsproject.wsservice.config;

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
	String apiPublicBaseUri; // 외부에서 zuul server에 접근할 때 사용되는 base uri. HATEOAS를 위한 link 정보를 생성할 때 사용된다.
	String jwtSignkey; // jwt token 생성에 사용되는 sign key
}
