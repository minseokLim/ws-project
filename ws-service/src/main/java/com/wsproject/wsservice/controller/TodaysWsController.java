package com.wsproject.wsservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wsproject.wsservice.dto.TodaysWsResponseDto;
import com.wsproject.wsservice.service.TodaysWsService;

import lombok.AllArgsConstructor;

/**
 * 오늘의 명언 Controller
 * @author mslim
 */
@AllArgsConstructor
@RestController
@RequestMapping("/v1.0/users/{userIdx}/todaysWs")
public class TodaysWsController {

	private TodaysWsService todaysWsService;
	
	/**
	 * 유저별 오늘의 명언 생성/업데이트 혹은 조회
	 * @param userIdx
	 * @return 오늘의 명언
	 */
	@GetMapping
	public ResponseEntity<TodaysWsResponseDto> selectTodaysWs(@PathVariable("userIdx") Long userIdx) {
		TodaysWsResponseDto result = todaysWsService.selectTodaysWs(userIdx);
		
		// TODO 불필요한 로직. 토큰 및 상관관계 ID 전파 테스트를 위해 추가
//		RestUtil restUtil = RestUtil.builder().url("/user-service/v1.0/users/maxIdx").build();
//		ResponseEntity<String> entity = restUtil.exchange();
//		log.debug("작동 잘 됩니끄아. body : " + entity.getBody());
		
		return ResponseEntity.ok(result);
	}
	
	/**
	 * 오늘의 명언을 새로고침
	 * @param ownerIdx
	 * @return
	 */
	@PostMapping
	public ResponseEntity<TodaysWsResponseDto> refreshTodaysWs(@PathVariable("userIdx") Long userIdx) {
		TodaysWsResponseDto result = todaysWsService.refreshTodaysWs(userIdx);
		
		return ResponseEntity.ok(result);
	}
}
