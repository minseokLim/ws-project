package com.wsproject.wsservice.domain.enums;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;

/**
 * 명언 종류<br>
 * 자기 개발, YOLO 등으로 명언의 종류를 구분해보려고 했으나, 구분하는 기준이 주관적일 수 있어서 현재는 거의 고려하고 있지 않은 상태
 * @author mslim
 *
 */
@Getter
public enum WsType {
	SELF_DEV("자기개발", 0), 
	YOLO("YOLO", 1), 
	ETC("기타", 2);
	
	private String desc;
	private int code;
	
	private static final Map<Integer, WsType> codeToEnum = Arrays.stream(values()).collect(toMap(WsType::getCode, e -> e));
	
	private WsType(String desc, int code) {
		this.desc = desc;
		this.code = code;
	}
	
	public static WsType ofCode(int code) {
		return Optional.ofNullable(codeToEnum.get(code)).orElseThrow(() -> new IllegalArgumentException(String.format("상태코드에 code=[%s]가 존재하지 않습니다.", code)));
	}
}
