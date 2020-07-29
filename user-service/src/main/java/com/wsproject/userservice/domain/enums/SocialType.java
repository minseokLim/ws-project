package com.wsproject.userservice.domain.enums;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;

/**
 * Social 서비스 종류 <br>
 * 사용자가 OAuth2를 통해 로그인할 수 있는 서비스 종류
 * @author mslim
 *
 */
@Getter
public enum SocialType {
	FACEBOOK("facebook", 0),
	GOOGLE("google", 1),
	KAKAO("kakao", 2),
	GITHUB("github", 3);
	
	private String registrationId;
	private int code;
	
	private static final Map<String, SocialType> registrationIdToEnum = Arrays.stream(values()).collect(toMap(SocialType::getRegistrationId, e -> e));
	private static final Map<Integer, SocialType> codeToEnum = Arrays.stream(values()).collect(toMap(SocialType::getCode, e -> e));
	
	private SocialType(String registrationId, int code) {
		this.registrationId = registrationId;
		this.code = code;
	}
	
	public static SocialType ofCode(int code) {
		return Optional.ofNullable(codeToEnum.get(code)).orElseThrow(() -> new RuntimeException(String.format("상태코드에 code=[%s]가 존재하지 않습니다.", code)));
	}
	
	public static SocialType ofRegistrationId(String registrationId) {
		return Optional.ofNullable(registrationIdToEnum.get(registrationId))
				.orElseThrow(() -> new RuntimeException(String.format("상태코드에 registrationId=[%s]가 존재하지 않습니다.", registrationId)));
	}
}
