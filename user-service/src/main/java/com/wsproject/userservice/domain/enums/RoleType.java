package com.wsproject.userservice.domain.enums;

public enum RoleType {
	ADMIN("ADMIN"),
	USER("USER");
	
	private String name;
	
	RoleType(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return name;
	}
}
