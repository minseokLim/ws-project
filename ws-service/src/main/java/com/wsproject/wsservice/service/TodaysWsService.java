package com.wsproject.wsservice.service;

import com.wsproject.wsservice.dto.TodaysWsDto;

public interface TodaysWsService {
	
	/**
	 * 사용자의 오늘의 명언을 반환
	 * @param ownerIdx
	 * @return 오늘의 명언
	 */
	public TodaysWsDto selectTodaysWs(Long ownerIdx);
	
	/**
	 * 관리자에 의한 명언 or 사용자가 등록한 명언 중 random으로 오늘의 명언이 결정되어 DB에 저장 및 반환
	 * @param ownerIdx
	 * @return 오늘의 명언
	 */
	public TodaysWsDto insertTodaysWs(Long ownerIdx);
}
