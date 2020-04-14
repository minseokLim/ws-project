package com.wsproject.authsvr.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wsproject.authsvr.domain.User;
import com.wsproject.authsvr.domain.enums.SocialType;
import com.wsproject.authsvr.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private UserRepository userRepository;
	
	public Optional<User> findByPrincipalAndSocialType(String principal, SocialType socialType) {
		return userRepository.findByPrincipalAndSocialType(principal, socialType);
	}
	
	public User save(User user) {
		return userRepository.save(user);
	}
}
