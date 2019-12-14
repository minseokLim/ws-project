package com.wsproject.wsservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.wsproject.wsservice.dto.WS_DTO;

public interface WS_Service {
		
	public PagedModel<WS_DTO> findWSesByUserEmail(String userEmail, Pageable pageable);
}
