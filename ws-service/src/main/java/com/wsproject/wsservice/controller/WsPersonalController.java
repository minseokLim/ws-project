package com.wsproject.wsservice.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wsproject.wsservice.dto.WsPersonalDto;
import com.wsproject.wsservice.service.WsPersonalService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/v1.0/api/users/{userEmail}/wses")
public class WsPersonalController {
	
	private WsPersonalService service;
	
	@GetMapping
	public ResponseEntity<PagedModel<WsPersonalDto>> selectWsPersonalPage(@PathVariable("userEmail") String userEmail, 
			@RequestParam(required = false) String search, @PageableDefault Pageable pageable) {
		PagedModel<WsPersonalDto> result = service.selectWsPersonals(userEmail, search, pageable);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.badRequest().build();
		}	
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<WsPersonalDto> selectWsPersonal(@PathVariable("userEmail") String userEmail, @PathVariable("id") Long id) {
		WsPersonalDto result = service.selectWsPersonal(userEmail, id);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	public ResponseEntity<WsPersonalDto> insertWsPersonal(@PathVariable("userEmail") String userEmail, @RequestBody WsPersonalDto dto) {
		
		if(!userEmail.equals(dto.getOwnerEmail())) {
			return ResponseEntity.badRequest().build();
		}
		
		WsPersonalDto result = service.insertWsPersonal(dto);
		return new ResponseEntity<WsPersonalDto>(result, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<WsPersonalDto> updateWsPersonal(@PathVariable("userEmail") String userEmail, @PathVariable("id") Long id, @RequestBody WsPersonalDto dto) {
		WsPersonalDto result = service.updateWsPersonal(userEmail, id, dto);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteWsPersonal(@PathVariable("userEmail") String userEmail, @PathVariable("id") Long id) {
		
		boolean result = service.deleteWsPersonal(userEmail, id);
		
		if(result) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
