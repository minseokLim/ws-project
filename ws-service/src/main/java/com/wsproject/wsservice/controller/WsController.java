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

/**
 * 명언(관리자 등록) Controller
 * @author mslim
 */
@AllArgsConstructor
@RestController
@RequestMapping("/v1.0/wses")
public class WsController {
		
	private WsService wsService;
		
	/**
	 * 명언 리스트 조회
	 * @param search
	 * @param pageable
	 * @return
	 */
	@GetMapping
	public ResponseEntity<PagedModel<WsDto>> selectWsList(@RequestParam(required = false) String search, @PageableDefault Pageable pageable) {
		PagedModel<WsDto> result = wsService.selectWsList(search, pageable);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.badRequest().build();
		}	
	}
	
	/**
	 * 명언 조회
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public ResponseEntity<WsDto> selectWs(@PathVariable("id") Long id) {
		WsDto result = wsService.selectWs(id);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * 명언 저장
	 * @param dto
	 * @return
	 */
	@PostMapping
	public ResponseEntity<WsDto> insertWs(@RequestBody WsDto dto) {
		WsDto result = wsService.insertWs(dto);
		return new ResponseEntity<WsDto>(result, HttpStatus.CREATED);
	}
	
	/**
	 * 명언 수정
	 * @param id
	 * @param dto
	 * @return
	 */
	@PutMapping("/{id}")
	public ResponseEntity<WsDto> updateWs(@PathVariable("id") Long id, @RequestBody WsDto dto) {
		WsDto result = wsService.updateWs(id, dto);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * 명언 삭제
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteWs(@PathVariable("id") Long id) {
		boolean result = wsService.deleteWs(id);
		
		if(result) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
