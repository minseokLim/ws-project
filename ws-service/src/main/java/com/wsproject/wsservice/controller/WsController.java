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

import com.wsproject.wsservice.dto.WsDto;
import com.wsproject.wsservice.service.WsService;

import lombok.AllArgsConstructor;;

@AllArgsConstructor
@RestController
@RequestMapping("/v1.0/wses")
public class WsController {
		
	private WsService service;
	
	@GetMapping
	public ResponseEntity<PagedModel<WsDto>> selectWsPage(@RequestParam(required = false) String search, @PageableDefault Pageable pageable) {
		PagedModel<WsDto> result = service.selectWses(search, pageable);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.badRequest().build();
		}	
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<WsDto> selectWs(@PathVariable("id") Long id) {
		WsDto result = service.selectWs(id);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	public ResponseEntity<WsDto> insertWs(@RequestBody WsDto dto) {
		WsDto result = service.insertWs(dto);
		return new ResponseEntity<WsDto>(result, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<WsDto> updateWs(@PathVariable("id") Long id, @RequestBody WsDto dto) {
		WsDto result = service.updateWs(id, dto);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteWs(@PathVariable("id") Long id) {
		boolean result = service.deleteWs(id);
		
		if(result) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
