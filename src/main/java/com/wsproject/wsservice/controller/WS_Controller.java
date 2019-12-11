package com.wsproject.wsservice.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wsproject.wsservice.dto.WS_DTO;
import com.wsproject.wsservice.service.WS_Service;

import lombok.AllArgsConstructor;;

@AllArgsConstructor
@RestController
@RequestMapping("/api/wses")
public class WS_Controller {
		
	private WS_Service service;
	
	@GetMapping
	public ResponseEntity<PagedModel<WS_DTO>> findWSesByUserEmail(@PageableDefault Pageable pageable, @RequestParam String userEmail) {
		return null;
	}	
}
