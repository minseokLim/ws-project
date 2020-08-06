package com.wsproject.authsvr.config;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.wsproject.authsvr.domain.enums.SocialType;
import com.wsproject.authsvr.util.SocialAuthenticationSuccessHandler;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private SocialAuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CharacterEncodingFilter filter = new CharacterEncodingFilter("UTF-8");
		
		http.authorizeRequests()
							// 인증 없이 접근할 수 있는 url목록
				.antMatchers("/login/**", "/images/**", "/js/**", "/css/**", "/oauth/**", "/invalidApproach", "/docs/**").permitAll()
				.anyRequest().authenticated()
			.and()
				.oauth2Login()
				.loginPage("/login")
				.successHandler(authenticationSuccessHandler)
			.and()
				.logout()
				.logoutUrl("/logout")
				.deleteCookies("JSESSIONID")
				.invalidateHttpSession(true)
			.and()
				.headers().frameOptions().disable()
			.and()
				.addFilterBefore(filter, CsrfFilter.class)
				.csrf().disable()
				.httpBasic();
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	/**
	 * OAuth2를 이용하여 로그인할 Social 서비스들의 정보를 인메모리에 저장한다.
	 * @param oAuth2ClientProperties
	 * @return
	 */
	@Bean
	public ClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties oAuth2ClientProperties) {
		List<ClientRegistration> registrations = oAuth2ClientProperties.getRegistration().keySet().stream()
												.map(registrationId -> SocialType.ofRegistrationId(registrationId).getRegistration(oAuth2ClientProperties))
												.filter(Objects::nonNull)
												.collect(toList());
		
		return new InMemoryClientRegistrationRepository(registrations);
	}
}
