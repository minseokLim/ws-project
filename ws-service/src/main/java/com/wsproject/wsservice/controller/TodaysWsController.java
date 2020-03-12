package com.wsproject.wsservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wsproject.wsservice.dto.TodaysWsDto;
import com.wsproject.wsservice.service.TodaysWsService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/v1.0/users/{ownerIdx}/todaysWs")
public class TodaysWsController {

	private TodaysWsService service;
	
	@GetMapping
	public ResponseEntity<TodaysWsDto> selectTodaysWs(@PathVariable("ownerIdx") Long ownerIdx) {
		TodaysWsDto result = service.selectTodaysWs(ownerIdx);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	public ResponseEntity<TodaysWsDto> insertTodaysWs(@PathVariable("ownerIdx") Long ownerIdx, @RequestBody TodaysWsDto dto) {
		
		if(ownerIdx != dto.getUserIdx()) {
			return ResponseEntity.badRequest().build();
		}
		
		TodaysWsDto result = service.insertTodaysWs(dto);
		return new ResponseEntity<TodaysWsDto>(result, HttpStatus.CREATED);
	}
}
