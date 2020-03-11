package com.wsproject.wsservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.wsproject.wsservice.dto.WsPslDto;

public interface WsPslService {
	public PagedModel<WsPslDto> selectWsPersonals(Long ownerIdx, String search, Pageable pageable);
	public WsPslDto selectWsPersonal(Long ownerIdx, Long id);
	public WsPslDto insertWsPersonal(WsPslDto dto);
	public WsPslDto updateWsPersonal(Long ownerIdx, Long id, WsPslDto dto);
	public boolean deleteWsPersonal(Long ownerIdx, Long id);
	public long countWsPersonal(Long ownerIdx);
}
