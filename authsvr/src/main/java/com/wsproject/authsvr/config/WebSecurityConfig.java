package com.wsproject.authsvr.config;

import static com.wsproject.authsvr.domain.enums.SocialType.FACEBOOK;
import static com.wsproject.authsvr.domain.enums.SocialType.GITHUB;
import static com.wsproject.authsvr.domain.enums.SocialType.GOOGLE;
import static com.wsproject.authsvr.domain.enums.SocialType.KAKAO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.wsproject.authsvr.util.CustomOAuth2Provider;
import com.wsproject.authsvr.util.SocialAuthenticationSuccessHandler;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private SocialAuthenticationSuccessHandler socialAuthenticationSuccessHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		CharacterEncodingFilter filter = new CharacterEncodingFilter("UTF-8");
		
		http.authorizeRequests()
				.antMatchers("/login/**", "/images/**", "/js/**", "/css/**", "/oauth/**", "/invalidApproach").permitAll()
				.anyRequest().authenticated()
			.and()
				.oauth2Login()
				.loginPage("/login")
				.successHandler(socialAuthenticationSuccessHandler)
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
	
	@Bean
	public ClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties oAuth2ClientProperties) {
		List<ClientRegistration> registrations = oAuth2ClientProperties.getRegistration().keySet().stream()
												.map(client -> getRegistration(oAuth2ClientProperties, client))
												.filter(Objects::nonNull)
												.collect(Collectors.toList());
		
		return new InMemoryClientRegistrationRepository(registrations);
	}
	
	private ClientRegistration getRegistration(OAuth2ClientProperties oAuth2ClientProperties, String client) {
		OAuth2ClientProperties.Registration registration = oAuth2ClientProperties.getRegistration().get(client);
		
		if(FACEBOOK.getValue().equals(client)) {
			return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
					.clientId(registration.getClientId())
					.clientSecret(registration.getClientSecret())
					// picture을 받아오기 위해 별도로 userInfoUri 설정
					.userInfoUri("https://graph.facebook.com/me?fields=id,name,email,picture")
					.build();
		} else if(GOOGLE.getValue().equals(client)) {
			return CommonOAuth2Provider.GOOGLE.getBuilder(client)
					.clientId(registration.getClientId())
					.clientSecret(registration.getClientSecret())
					.build();
		} else if(KAKAO.getValue().equals(client)) {
			return CustomOAuth2Provider.KAKAO.getBuilder(client)
					.clientId(registration.getClientId())
					.build();
		} else if(GITHUB.getValue().equals(client)) {
			return CommonOAuth2Provider.GITHUB.getBuilder(client)
					.clientId(registration.getClientId())
					.clientSecret(registration.getClientSecret())
					.build();
		}
		
		return null;
	}
}
