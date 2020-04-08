package com.wsproject.authsvr.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.wsproject.authsvr.util.SessionInvalidateFilter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	private static final long MAX_AGE_SECONDS = 0;
	
	private SessionInvalidateFilter sessionInvalidateFilter;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("GET", "POST", "PUT", "DELETE")
				.allowedHeaders("*")
				.allowCredentials(true)
				.maxAge(MAX_AGE_SECONDS);
	}
	
	@Bean
	public FilterRegistrationBean<SessionInvalidateFilter> filterRegistrationBean() {
		FilterRegistrationBean<SessionInvalidateFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(sessionInvalidateFilter);
		registrationBean.addUrlPatterns("/oauth/authorize*");
		return registrationBean;
	}
}
