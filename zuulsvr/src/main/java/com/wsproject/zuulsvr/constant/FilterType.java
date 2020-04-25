package com.wsproject.zuulsvr.constant;

import org.springframework.stereotype.Component;

/**
 * 필터 타입
 * @author mslim
 *
 */
@Component
public class FilterType {
	public static final String PRE_FILTER_TYPE = "pre";
    public static final String POST_FILTER_TYPE = "post";
    public static final String ROUTE_FILTER_TYPE = "route";
}
