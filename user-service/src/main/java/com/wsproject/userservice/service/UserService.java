package com.wsproject.userservice.service;

import java.util.Optional;

import com.wsproject.userservice.domain.User;

public interface UserService {
	public Optional<User> selectUser(Long userIdx);
	public Long getMaxUserIdx();
}
