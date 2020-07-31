package com.wsproject.wsservice.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.wsproject.wsservice.dto.WsPrivateRequestDto;
import com.wsproject.wsservice.dto.WsPrivateResponseDto;

public interface WsPrivateService {
	public PagedModel<WsPrivateResponseDto> selectWsPrivateList(Long ownerIdx, String search, Pageable pageable);
	public Optional<WsPrivateResponseDto> selectWsPrivate(Long ownerIdx, Long id);
	public WsPrivateResponseDto insertWsPrivate(WsPrivateRequestDto dto);
	public Optional<WsPrivateResponseDto> updateWsPrivate(Long ownerIdx, Long id, WsPrivateRequestDto dto);
	public void deleteWsPrivate(Long ownerIdx, Long id);
}
