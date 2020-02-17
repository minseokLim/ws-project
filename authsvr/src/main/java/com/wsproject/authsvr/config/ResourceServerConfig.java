package com.wsproject.authsvr.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

//@Configuration
//@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/login/**", "/images/**", "/js/**", "/css/**", "/oauth/**", "/oauth2/**").permitAll()
			.antMatchers("/users/**").access("#oauth2.hasScope('profile')")
			.anyRequest().authenticated();
	}
}
