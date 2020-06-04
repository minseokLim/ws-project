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

import com.wsproject.wsservice.dto.WsPrivateRequestDto;
import com.wsproject.wsservice.dto.WsPrivateResponseDto;
import com.wsproject.wsservice.service.WsPrivateService;

import lombok.AllArgsConstructor;

/**
 * 명언(사용자 등록) Controller
 * @author mslim
 *
 */
@AllArgsConstructor
@RestController
@RequestMapping("/v1.0/users/{ownerIdx}/wses")
public class WsPrivateController {
	
	private WsPrivateService wsPrivateService;
	
	/**
	 * 사용자 명언 리스트 조회
	 * @param ownerIdx
	 * @param search
	 * @param pageable
	 * @return
	 */
	@GetMapping
	public ResponseEntity<PagedModel<WsPrivateResponseDto>> selectWsPrivateList(@PathVariable("ownerIdx") Long ownerIdx, 
			@RequestParam(required = false) String search, @PageableDefault Pageable pageable) {
		PagedModel<WsPrivateResponseDto> result = wsPrivateService.selectWsPrivateList(ownerIdx, search, pageable);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}	
	}
	
	/**
	 * 사용자 명언 조회
	 * @param ownerIdx
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public ResponseEntity<WsPrivateResponseDto> selectWsPrivate(@PathVariable("ownerIdx") Long ownerIdx, @PathVariable("id") Long id) {
		WsPrivateResponseDto result = wsPrivateService.selectWsPrivate(ownerIdx, id);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * 사용자 명언 저장
	 * @param ownerIdx
	 * @param dto
	 * @return
	 */
	@PostMapping
	public ResponseEntity<WsPrivateResponseDto> insertWsPrivate(@PathVariable("ownerIdx") Long ownerIdx, @RequestBody WsPrivateRequestDto dto) {
		
		if(dto.getOwnerIdx() == null) {
			dto.setOwnerIdx(ownerIdx);
		} else if(!dto.getOwnerIdx().equals(ownerIdx)) {
			return ResponseEntity.badRequest().build();
		}
		
		WsPrivateResponseDto result = wsPrivateService.insertWsPrivate(dto);
		
		return new ResponseEntity<WsPrivateResponseDto>(result, HttpStatus.CREATED);
	}
	
	/**
	 * 사용자 명언 수정
	 * @param ownerIdx
	 * @param id
	 * @param dto
	 * @return
	 */
	@PutMapping("/{id}")
	public ResponseEntity<WsPrivateResponseDto> updateWsPrivate(@PathVariable("ownerIdx") Long ownerIdx, @PathVariable("id") Long id, @RequestBody WsPrivateRequestDto dto) {
		
		if(dto.getOwnerIdx() == null) {
			dto.setOwnerIdx(ownerIdx);
		} else if(!dto.getOwnerIdx().equals(ownerIdx)) {
			return ResponseEntity.badRequest().build();
		}
		
		WsPrivateResponseDto result = wsPrivateService.updateWsPrivate(ownerIdx, id, dto);
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * 사용자 명언 삭제
	 * @param ownerIdx
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteWsPrivate(@PathVariable("ownerIdx") Long ownerIdx, @PathVariable("id") Long id) {
		wsPrivateService.deleteWsPrivate(ownerIdx, id);
		
		return ResponseEntity.noContent().build();
	}
}