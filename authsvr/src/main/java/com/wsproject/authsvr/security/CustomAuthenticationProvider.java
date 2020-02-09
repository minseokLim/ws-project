package com.wsproject.authsvr.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.wsproject.authsvr.domain.User;
import com.wsproject.authsvr.repository.UserRepository;

import lombok.AllArgsConstructor;

// not used
@AllArgsConstructor
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		User user = userRepository.findByUid(name).orElseThrow(() -> new UsernameNotFoundException("user doesn't exist"));
		
		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new BadCredentialsException("password is not valid");
		}
		
		return new UsernamePasswordAuthenticationToken(name, password, user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
