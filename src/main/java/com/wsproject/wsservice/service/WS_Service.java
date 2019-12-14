package com.wsproject.wsservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.wsproject.wsservice.dto.WS_DTO;

public interface WS_Service {
		
	public PagedModel<WS_DTO> selectWSes(String userEmail, Pageable pageable);
	public WS_DTO selectWSById(Long id);
	public WS_DTO insertWS(WS_DTO dto);
	public WS_DTO updateWSById(Long id, WS_DTO dto);
	public boolean deleteWSById(Long id);
}
