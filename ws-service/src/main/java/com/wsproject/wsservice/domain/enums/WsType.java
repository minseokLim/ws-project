package com.wsproject.wsservice.domain.enums;

import java.util.Arrays;

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
	
	private WsType(String desc, int code) {
		this.desc = desc;
		this.code = code;
	}
	
	public static WsType ofCode(int code) {
		return Arrays.stream(WsType.values())
					.filter(v -> v.getCode() == code)
					.findAny()
					.orElseThrow(() -> new RuntimeException(String.format("상태코드에 code=[%s]가 존재하지 않습니다.", code)));
	}
}
