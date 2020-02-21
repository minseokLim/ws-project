package com.wsproject.userservice.controller;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.wsproject.userservice.domain.User;
import com.wsproject.userservice.domain.enums.SocialType;
import com.wsproject.userservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RepositoryRestController
@RequiredArgsConstructor
public class UserController {
	
	@Value("${security.oauth2.jwt.signkey}")
	private String signKey;
	
	private final UserRepository userRepository;
	
	private final Gson gson;
	
	@GetMapping("/users/me")
	public ResponseEntity<User> getLoginUserInfo(@RequestHeader(value="Authorization") String header) {
		String accessToken = header.split(" ")[1];
		String payload = accessToken.split("\\.")[1];
		@SuppressWarnings("unchecked")
		Map<String, Object> tokenInfo = gson.fromJson(new String(Base64.getDecoder().decode(payload)), Map.class);
		
		Long userIdx = Long.parseLong((String) tokenInfo.get("user_name"));
		
		Optional<User> user = userRepository.findById(userIdx);
		
		if(user.isPresent()) {
			return new ResponseEntity<User>(user.get(), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/users/search/findByPrincipalAndSocialType")
	public ResponseEntity<User> findSocialUser(@RequestParam("principal") String principal, @RequestParam("socialType") SocialType socialType) {
		Optional<User> user = userRepository.findByPrincipalAndSocialType(principal, socialType);
		
		if(user.isPresent()) {
			return new ResponseEntity<User>(user.get(), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
