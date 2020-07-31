package com.wsproject.wsservice.controller;

import static com.wsproject.wsservice.util.CommonUtil.getErrorMessages;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
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
	
	private static final String OWNER_IDX_DIFFER_ERR = "OwnerIdx in dto is different from the one in uri as a path variable";
	
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
		
		return ResponseEntity.ok(result); // TODO result가 null일 수 있을까? 없을 거 같은데...
	}
	
	/**
	 * 사용자 명언 조회
	 * @param ownerIdx
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public ResponseEntity<WsPrivateResponseDto> selectWsPrivate(@PathVariable("ownerIdx") Long ownerIdx, @PathVariable("id") Long id) {
		
		Optional<WsPrivateResponseDto> result = wsPrivateService.selectWsPrivate(ownerIdx, id);
		
		return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	/**
	 * 사용자 명언 저장
	 * @param ownerIdx
	 * @param dto
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> insertWsPrivate(@PathVariable("ownerIdx") Long ownerIdx, @Valid @RequestBody WsPrivateRequestDto dto, 
											Errors errors) {
		if(errors.hasErrors()) {
			return new ResponseEntity<String>(getErrorMessages(errors, "|"), HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getOwnerIdx() == null) {
			dto.setOwnerIdx(ownerIdx);
		} else if(!dto.getOwnerIdx().equals(ownerIdx)) {
			return new ResponseEntity<String>(OWNER_IDX_DIFFER_ERR, HttpStatus.BAD_REQUEST);
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
	public ResponseEntity<?> updateWsPrivate(@PathVariable("ownerIdx") Long ownerIdx, @PathVariable("id") Long id, 
											@Valid @RequestBody WsPrivateRequestDto dto, Errors errors) {
		
		if(errors.hasErrors()) {
			return new ResponseEntity<String>(getErrorMessages(errors, "|"), HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getOwnerIdx() == null) {
			dto.setOwnerIdx(ownerIdx);
		} else if(!dto.getOwnerIdx().equals(ownerIdx)) {
			return new ResponseEntity<String>(OWNER_IDX_DIFFER_ERR, HttpStatus.BAD_REQUEST);
		}
		
		Optional<WsPrivateResponseDto> result = wsPrivateService.updateWsPrivate(ownerIdx, id, dto);
		return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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
