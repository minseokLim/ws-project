package com.wsproject.userservice.domain.enums;

import java.util.Arrays;

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
	
	private String value;
	private int code;
	
	private SocialType(String value, int code) {
		this.value = value;
		this.code = code;
	}
	
	public static SocialType ofCode(int code) {
		return Arrays.stream(SocialType.values())
					.filter(v -> v.getCode() == code)
					.findAny()
					.orElseThrow(() -> new RuntimeException(String.format("상태코드에 code=[%s]가 존재하지 않습니다.", code)));
	}
}
