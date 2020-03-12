package com.wsproject.wsservice.service;

import com.wsproject.wsservice.dto.TodaysWsDto;

public interface TodaysWsService {
	public TodaysWsDto selectTodaysWs(Long ownerIdx);
	public TodaysWsDto insertTodaysWs(TodaysWsDto dto);
}
