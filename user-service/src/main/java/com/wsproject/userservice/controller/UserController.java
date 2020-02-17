package com.wsproject.userservice.controller;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.wsproject.userservice.domain.User;

@RepositoryRestController
public class UserController {
	@GetMapping("/users/me")
	public User getLoginUserInfo(@RequestHeader(value="Authorization") String accessToken) {
		return null;
	}
}
