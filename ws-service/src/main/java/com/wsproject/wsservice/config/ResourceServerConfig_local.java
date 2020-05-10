package com.wsproject.wsservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import lombok.AllArgsConstructor;

/**
 * 로컬에서 인증서버 영향없이 테스트하기 위해 추가
 * @author mslim
 *
 */
@Configuration
@Profile("local")
@AllArgsConstructor
@EnableResourceServer
public class ResourceServerConfig_local  extends ResourceServerConfigurerAdapter {
	
	private CustomProperties properties;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.anyRequest().permitAll();
	}
	 
	@Bean
	public TokenStore tokenStore() {
	    return new JwtTokenStore(accessTokenConverter());
	}
	 
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
	    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
	    converter.setSigningKey(properties.getJwtSignkey());
	    return converter;
	}
}
