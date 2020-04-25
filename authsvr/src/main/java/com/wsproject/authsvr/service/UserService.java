package com.wsproject.authsvr.service;

import java.util.Optional;

import com.wsproject.authsvr.domain.User;
import com.wsproject.authsvr.domain.enums.SocialType;

public interface UserService {
	public Optional<User> selectUserByPrincipalAndSocialType(String principal, SocialType socialType);
	public User insertUser(User user);
}
