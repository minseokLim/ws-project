package com.wsproject.authsvr.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wsproject.authsvr.domain.User;
import com.wsproject.authsvr.domain.enums.SocialType;
import com.wsproject.authsvr.repository.UserRepository;
import com.wsproject.authsvr.service.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	
	@Override
	public Optional<User> selectUserByPrincipalAndSocialType(String principal, SocialType socialType) {
		return userRepository.findByPrincipalAndSocialType(principal, socialType);
	}
	
	@Override
	public User insertUser(User user) {
		return userRepository.save(user);
	}
}
