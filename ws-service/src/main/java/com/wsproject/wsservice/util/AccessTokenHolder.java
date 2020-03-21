package com.wsproject.wsservice.util;

/**
 * 토큰정보를 ThreadLocal에 저장해놓음
 * @author mslim
 *
 */
public class AccessTokenHolder {
	private static final ThreadLocal<String> accessToken = new ThreadLocal<String>();
	
	public static final String getAccessToken() {
		return accessToken.get();
	}
	
	public static final void setAccessToken(String value) {
		accessToken.set(value);
	}
}
