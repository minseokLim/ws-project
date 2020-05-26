package com.wsproject.wsservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.wsproject.wsservice.dto.WsAdminRequestDto;
import com.wsproject.wsservice.dto.WsAdminResponseDto;

public interface WsAdminService {		
	public PagedModel<WsAdminResponseDto> selectWsAdminList(String search, Pageable pageable);
	public WsAdminResponseDto selectWsAdmin(Long id);
	public WsAdminResponseDto insertWsAdmin(WsAdminRequestDto dto);
	public WsAdminResponseDto updateWsAdmin(Long id, WsAdminRequestDto dto);
	public void deleteWsAdmin(Long id);
}
