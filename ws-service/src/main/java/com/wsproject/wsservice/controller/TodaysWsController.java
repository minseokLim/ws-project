package com.wsproject.wsservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wsproject.wsservice.dto.TodaysWsDto;
import com.wsproject.wsservice.service.TodaysWsService;
import com.wsproject.wsservice.util.RestUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mslim
 * 오늘의 명언 Controller
 */
@AllArgsConstructor
@RestController
@RequestMapping("/v1.0/users/{ownerIdx}/todaysWs")
@Slf4j
public class TodaysWsController {

	private TodaysWsService todaysWsService;
	
	/**
	 * 유저별 오늘의 명언 조회
	 * @param ownerIdx
	 * @return 오늘의 명언
	 */
	@GetMapping
	public ResponseEntity<TodaysWsDto> selectTodaysWs(@PathVariable("ownerIdx") Long ownerIdx) {
		
		TodaysWsDto result = todaysWsService.selectTodaysWs(ownerIdx);
		
		// TODO 불필요한 로직. 토큰 및 상관관계 ID 전파 테스트를 위해 추가
		RestUtil restUtil = RestUtil.builder().url("/user-service/v1.0/users/maxIdx").get().build();
		ResponseEntity<String> entity = restUtil.exchange();
		log.debug("작동 잘 됩니끄아. body : " + entity.getBody());
		
		if(result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * 오늘의 명언 생성/업데이트 혹은 조회
	 * @param ownerIdx
	 * @return
	 */
	@PostMapping
	public ResponseEntity<TodaysWsDto> insertTodaysWs(@PathVariable("ownerIdx") Long ownerIdx) {
		
		TodaysWsDto result = todaysWsService.selectTodaysWs(ownerIdx);
		
		return new ResponseEntity<TodaysWsDto>(result, HttpStatus.CREATED);
	}
}
