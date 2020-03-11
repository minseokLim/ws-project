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

import com.wsproject.wsservice.dto.WsPslDto;
import com.wsproject.wsservice.service.WsPslService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/v1.0/users/{ownerIdx}/wses")
public class WsPslController {
	
	private WsPslService service;
	
	@GetMapping
	public ResponseEntity<PagedModel<WsPslDto>> selectWsPersonalPage(@PathVariable("ownerIdx") Long ownerIdx, 
			@RequestParam(required = false) String search, @PageableDefault Pageable pageable) {
		PagedModel<WsPslDto> result = service.selectWsPersonals(ownerIdx, search, pageable);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.badRequest().build();
		}	
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<WsPslDto> selectWsPersonal(@PathVariable("ownerIdx") Long ownerIdx, @PathVariable("id") Long id) {
		WsPslDto result = service.selectWsPersonal(ownerIdx, id);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	public ResponseEntity<WsPslDto> insertWsPersonal(@PathVariable("ownerIdx") Long ownerIdx, @RequestBody WsPslDto dto) {
		
		if(ownerIdx != dto.getOwnerIdx()) {
			return ResponseEntity.badRequest().build();
		}
		
		WsPslDto result = service.insertWsPersonal(dto);
		return new ResponseEntity<WsPslDto>(result, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<WsPslDto> updateWsPersonal(@PathVariable("ownerIdx") Long ownerIdx, @PathVariable("id") Long id, @RequestBody WsPslDto dto) {
		WsPslDto result = service.updateWsPersonal(ownerIdx, id, dto);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteWsPersonal(@PathVariable("ownerIdx") Long ownerIdx, @PathVariable("id") Long id) {
		
		boolean result = service.deleteWsPersonal(ownerIdx, id);
		
		if(result) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/count")
	public ResponseEntity<Long> count(@PathVariable("ownerIdx") Long ownerIdx) {
		long result = service.countWsPersonal(ownerIdx);
		return ResponseEntity.ok(result);
	}
}
