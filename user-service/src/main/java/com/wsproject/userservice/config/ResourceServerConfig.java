package com.wsproject.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig  extends ResourceServerConfigurerAdapter {
	
	@Value("${security.oauth2.jwt.signkey}")
	private String signKey;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/v1.0/users/me").access("#oauth2.hasScope('profile')")
			.antMatchers("/v1.0/users/search/findByPrincipalAndSocialType").permitAll()
			.antMatchers(HttpMethod.POST, "/v1.0/users").permitAll()
			.anyRequest().authenticated();
	}
	 
	@Bean
	public TokenStore tokenStore() {
	    return new JwtTokenStore(accessTokenConverter());
	}
	 
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
	    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
	    converter.setSigningKey(signKey);
	    return converter;
	}
}
