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

import com.wsproject.wsservice.dto.WS_DTO;
import com.wsproject.wsservice.service.WS_Service;

import lombok.AllArgsConstructor;;

@AllArgsConstructor
@RestController
@RequestMapping("/api/wses")
public class WS_Controller {
		
	private WS_Service service;
	
	@GetMapping
	public ResponseEntity<PagedModel<WS_DTO>> selectWSes(@RequestParam(required = false) String userEmail, @PageableDefault Pageable pageable) {
		PagedModel<WS_DTO> model = service.selectWSes(userEmail, pageable);
		return ResponseEntity.ok(model);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<WS_DTO> selectWSById(@PathVariable("id") Long id) {
		WS_DTO result = service.selectWSById(id);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	public ResponseEntity<WS_DTO> insertWS(@RequestBody WS_DTO dto) {
		WS_DTO result = service.insertWS(dto);
		return new ResponseEntity<WS_DTO>(result, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<WS_DTO> updateWSById(@PathVariable("id") Long id, @RequestBody WS_DTO dto) {
		WS_DTO result = service.updateWSById(id, dto);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteWSById(@PathVariable("id") Long id) {
		boolean result = service.deleteWSById(id);
		
		if(result) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
