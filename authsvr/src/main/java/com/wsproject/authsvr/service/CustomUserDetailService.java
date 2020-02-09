package com.wsproject.authsvr.service;

import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wsproject.authsvr.domain.User;
import com.wsproject.authsvr.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

	private UserRepository userRepository;
	private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUid(username).orElseThrow(() -> new UsernameNotFoundException("user doesn't exist"));
		detailsChecker.check(user);
		return user;
	}	
}
