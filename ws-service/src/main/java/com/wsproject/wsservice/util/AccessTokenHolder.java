package com.wsproject.wsservice.util;

/**
 * 토큰정보를 ThreadLocal에 저장해놓음
 * @author mslim
 *
 */
public class AccessTokenHolder {
	private static final ThreadLocal<String> authorization = new ThreadLocal<String>();
	
	public static final String getAuthorization() {
		return authorization.get();
	}
	
	public static final void setAuthorization(String value) {
		authorization.set(value);
	}
}
