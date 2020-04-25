package com.wsproject.userservice.controller;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.wsproject.userservice.domain.User;
import com.wsproject.userservice.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/v1.0/users")
public class UserController {
	
	private final UserService userService;
	
	private final Gson gson;
	
	/**
	 * 로그인 이후 사용자 정보를 얻기 위해 호출하는 부분
	 * @param authorizatioin
	 * @return 로그인 사용자 정보
	 */
	@GetMapping("/me")
	public ResponseEntity<User> selectLoginUser(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizatioin) {
		String accessToken = authorizatioin.split(" ")[1];
		String payload = accessToken.split("\\.")[1];
		
		// Base64로 인코딩되어 있는 JWT로부터 로그인한 사용자의 userIdx를 추출한다.
		@SuppressWarnings("unchecked")
		Map<String, Object> tokenInfo = gson.fromJson(new String(Base64.getDecoder().decode(payload)), Map.class);
		Long userIdx = Long.parseLong((String) tokenInfo.get("user_name"));
		
		Optional<User> user = userService.selectUser(userIdx);
		
		if(user.isPresent()) {
			return new ResponseEntity<User>(user.get(), HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * @return AUTO_INCREMENT로 설정되어 있는 사용자의 ID 중 가장 큰 값을 반환
	 */
	@GetMapping("/maxIdx")
	public ResponseEntity<Long> getMaxUserIdx() {
		Long maxIdx = userService.getMaxUserIdx();
		
		return ResponseEntity.ok(maxIdx);
	}
}
