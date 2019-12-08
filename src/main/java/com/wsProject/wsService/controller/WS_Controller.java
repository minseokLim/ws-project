package com.wsProject.wsService.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wsProject.wsService.dto.WS_DTO;
import com.wsProject.wsService.service.WS_Service;

import lombok.AllArgsConstructor;;

@AllArgsConstructor
@RepositoryRestController
@RequestMapping("/wses")
public class WS_Controller {
		
	private WS_Service service;
	
	@GetMapping
	public ResponseEntity<PagedModel<WS_DTO>> getWSes(@PageableDefault Pageable pageable, @RequestParam String userEmail) {
		PagedModel<WS_DTO> model = service.getWSes(pageable, userEmail);
		
		return ResponseEntity.ok(model);
	}
	
	@GetMapping("/{id}") 
	public ResponseEntity<WS_DTO> getWSById(@PathVariable("id") Long id, @RequestParam String userEmail) {
		WS_DTO ws = service.getWSById(id, userEmail);
		
		return ResponseEntity.ok(ws);
	}
	
}
