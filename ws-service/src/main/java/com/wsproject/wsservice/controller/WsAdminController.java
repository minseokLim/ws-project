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

import com.wsproject.wsservice.dto.WsAdminRequestDto;
import com.wsproject.wsservice.dto.WsAdminResponseDto;
import com.wsproject.wsservice.service.WsAdminService;

import lombok.AllArgsConstructor;;

/**
 * 명언(관리자 등록) Controller
 * @author mslim
 */
@AllArgsConstructor
@RestController
@RequestMapping("/v1.0/admin/wses")
public class WsAdminController {
		
	private WsAdminService wsAdminService;
		
	/**
	 * 명언 리스트 조회
	 * @param search
	 * @param pageable
	 * @return
	 */
	@GetMapping
	public ResponseEntity<PagedModel<WsAdminResponseDto>> selectWsAdminList(@RequestParam(required = false) String search, @PageableDefault Pageable pageable) {
		PagedModel<WsAdminResponseDto> result = wsAdminService.selectWsAdminList(search, pageable);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}	
	}
	
	/**
	 * 명언 조회
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public ResponseEntity<WsAdminResponseDto> selectWsAdmin(@PathVariable("id") Long id) {
		WsAdminResponseDto result = wsAdminService.selectWsAdmin(id);
		
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
	public ResponseEntity<WsAdminResponseDto> insertWsAdmin(@RequestBody WsAdminRequestDto dto) {
		WsAdminResponseDto result = wsAdminService.insertWsAdmin(dto);
		
		return new ResponseEntity<WsAdminResponseDto>(result, HttpStatus.CREATED);
	}
	
	/**
	 * 명언 수정
	 * @param id
	 * @param dto
	 * @return
	 */
	@PutMapping("/{id}")
	public ResponseEntity<WsAdminResponseDto> updateWsAdmin(@PathVariable("id") Long id, @RequestBody WsAdminRequestDto dto) {
		WsAdminResponseDto result = wsAdminService.updateWsAdmin(id, dto);
		
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
	public ResponseEntity<Void> deleteWsAdmin(@PathVariable("id") Long id) {
		wsAdminService.deleteWsAdmin(id);
		
		return ResponseEntity.noContent().build();
	}
}
