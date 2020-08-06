package com.wsproject.authsvr.service.impl;

import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wsproject.authsvr.domain.User;
import com.wsproject.authsvr.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findById(Long.parseLong(username)).orElseThrow(() -> new UsernameNotFoundException(String.format("user [%s] doesn't exist", username)));	
		
		AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();
		detailsChecker.check(user);
		
		return user;
	}
}
