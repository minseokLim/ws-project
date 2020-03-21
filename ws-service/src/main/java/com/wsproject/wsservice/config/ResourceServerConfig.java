package com.wsproject.wsservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.wsproject.wsservice.property.CustomProperties;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableResourceServer
public class ResourceServerConfig  extends ResourceServerConfigurerAdapter {
	
	private CustomProperties properties;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()	
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