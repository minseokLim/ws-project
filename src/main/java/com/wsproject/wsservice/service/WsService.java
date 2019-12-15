package com.wsproject.wsservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.wsproject.wsservice.dto.WsDto;

public interface WsService {
		
	public PagedModel<WsDto> selectWses(String userEmail, Pageable pageable);
	public WsDto selectWsById(Long id);
	public WsDto insertWs(WsDto dto);
	public WsDto updateWsById(Long id, WsDto dto);
	public boolean deleteWsById(Long id);
}
