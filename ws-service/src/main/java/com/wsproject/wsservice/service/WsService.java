package com.wsproject.wsservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.wsproject.wsservice.dto.WsDto;

public interface WsService {		
	public PagedModel<WsDto> selectWses(String search, Pageable pageable);
	public WsDto selectWs(Long id);
	public WsDto insertWs(WsDto dto);
	public WsDto updateWs(Long id, WsDto dto);
	public boolean deleteWs(Long id);
}
