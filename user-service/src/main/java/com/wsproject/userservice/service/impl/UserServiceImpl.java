package com.wsproject.userservice.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wsproject.userservice.domain.User;
import com.wsproject.userservice.repository.UserRepository;
import com.wsproject.userservice.service.UserService;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;

	@Override
	public Optional<User> selectUser(Long userIdx) {
		return userRepository.findById(userIdx);
	}

	@Override
	public Long getMaxUserIdx() {
		return userRepository.getMaxUserIdx();
	}
}
