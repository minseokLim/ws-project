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

@Configuration
@Profile("!local")
@AllArgsConstructor
@EnableResourceServer
public class ResourceServerConfig  extends ResourceServerConfigurerAdapter {
	
	private CustomProperties properties;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/docs/**").permitAll() // API 문서에 대해서는 모두 접근 가능
			.antMatchers("/v1.0/admin/wses/**").hasRole("ADMIN") // 관리자만 명언서비스(각 사용자별 명언이 아닌 모든 사용자에게 노출되는 명언)에 접근할 수 있음
			.anyRequest().authenticated();
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
