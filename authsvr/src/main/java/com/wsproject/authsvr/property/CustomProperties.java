package com.wsproject.authsvr.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {
	String apiBaseUri;
	String jwtSignkey;
}
