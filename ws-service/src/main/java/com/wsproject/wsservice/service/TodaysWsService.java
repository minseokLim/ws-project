package com.wsproject.wsservice.service;

import com.wsproject.wsservice.dto.TodaysWsDto;

public interface TodaysWsService {
	
	/**
	 * 사용자의 오늘의 명언을 반환<br>
	 * 처음 로그인해서 사용자의 명언 정보가 없거나, 테이블에 저장된 명언이 어제의 것일 경우, 자동으로 insert 후에 결과 값을 반환한다.<br>
	 * 오늘의 명언은 관리자에 의한 명언 or 사용자가 등록한 명언 중 random으로 오늘의 명언이 결정된다.
	 * @param ownerIdx
	 * @return 오늘의 명언
	 */
	public TodaysWsDto selectTodaysWs(Long ownerIdx);
	
	public TodaysWsDto refreshTodaysWs(Long ownerIdx);
}
