package com.wsproject.userservice.domain.enums;

public enum RoleType {
	ADMIN("ADMIN"),
	USER("USER");
	
	private final static String PREFIX = "ROLE_";
	private String name;
	
	RoleType(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return PREFIX + name;
	}
}
