package com.wsproject.authsvr.util;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtil {

	/**
	 * Request의 진짜 IP(Nginx등의 IP가 아닌)를 구하기 위한 메서드
	 * @param request
	 * @return 진짜 IP
	 */
	public static String getClientIP(HttpServletRequest request) {
	    String ip = request.getHeader("X-Forwarded-For");
	    log.debug("> X-FORWARDED-FOR : " + ip);

	    if (ip == null) {
	        ip = request.getHeader("Proxy-Client-IP");
	        log.debug("> Proxy-Client-IP : " + ip);
	    }
	    
	    if (ip == null) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	        log.debug(">  WL-Proxy-Client-IP : " + ip);
	    }
	    
	    if (ip == null) {
	        ip = request.getHeader("HTTP_CLIENT_IP");
	        log.debug("> HTTP_CLIENT_IP : " + ip);
	    }
	    
	    if (ip == null) {
	        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	        log.debug("> HTTP_X_FORWARDED_FOR : " + ip);
	    }
	    
	    if (ip == null) {
	        ip = request.getRemoteAddr();
	        log.debug("> getRemoteAddr : "+ip);
	    }
	    
	    log.debug("> Result : IP Address : "+ip);

	    return ip;
	}
	
    /**
     * String.valueOf(null) = "null" <br>
     * 위와 같은 경우에 대처하기 위한 함수 <br>
     * 보통 때는 String.valueOf 함수와 똑같으나 obj에 null이 들어올 경우 null을 반환한다.
     * @param obj
     * @return
     */
    public static String convertObjToStr(Object obj) {
    	return obj != null ? String.valueOf(obj) : null;
    }
}
