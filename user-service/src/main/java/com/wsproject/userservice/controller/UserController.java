package com.wsproject.userservice.controller;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.wsproject.userservice.domain.User;
import com.wsproject.userservice.repository.UserRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/v1.0/users")
public class UserController {
	
	private final UserRepository userRepository;
	
	private final Gson gson;
	
	@GetMapping("/me")
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
}
