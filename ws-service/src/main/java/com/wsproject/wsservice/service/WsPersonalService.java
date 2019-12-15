package com.wsproject.wsservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.wsproject.wsservice.dto.WsPersonalDto;

public interface WsPersonalService {
	public PagedModel<WsPersonalDto> selectWsPersonals(String userEmail, String search, Pageable pageable);
	public WsPersonalDto selectWsPersonal(String userEmail, Long id);
	public WsPersonalDto insertWsPersonal(WsPersonalDto dto);
	public WsPersonalDto updateWsPersonal(String userEmail, Long id, WsPersonalDto dto);
	public boolean deleteWsPersonal(String userEmail, Long id);
}
