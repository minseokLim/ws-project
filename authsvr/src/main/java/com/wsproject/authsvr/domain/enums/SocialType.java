package com.wsproject.authsvr.domain.enums;

public enum SocialType {
	FACEBOOK("facebook"),
	GOOGLE("google"),
	KAKAO("kakao"),
	GITHUB("github");
	
	private String name;
	
	SocialType(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return name;
	}
}
