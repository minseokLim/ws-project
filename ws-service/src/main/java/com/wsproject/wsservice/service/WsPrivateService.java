package com.wsproject.wsservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.wsproject.wsservice.dto.WsPrivateRequestDto;
import com.wsproject.wsservice.dto.WsPrivateResponseDto;

public interface WsPrivateService {
	public PagedModel<WsPrivateResponseDto> selectWsPrivateList(Long ownerIdx, String search, Pageable pageable);
	public WsPrivateResponseDto selectWsPrivate(Long ownerIdx, Long id);
	public WsPrivateResponseDto insertWsPrivate(WsPrivateRequestDto dto);
	public WsPrivateResponseDto updateWsPrivate(Long ownerIdx, Long id, WsPrivateRequestDto dto);
	public void deleteWsPrivate(Long ownerIdx, Long id);
}
