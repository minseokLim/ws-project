package com.wsproject.wsservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wsproject.wsservice.service.LikeService;

import lombok.AllArgsConstructor;

/**
 * 좋아요 관련 Controller
 * @author mslim
 *
 */
@AllArgsConstructor
@RestController
@RequestMapping("/v1.0/users/{userIdx}/wses/{wsId}/like")
public class LikeController {

	private LikeService likeService;

	/**
	 * 사용자가 명언에 대해 좋아요 표시를 한다.
	 * @param userIdx
	 * @param wsId
	 * @param privateFlag
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Boolean> insertLike(@PathVariable("userIdx") Long userIdx, @PathVariable("wsId") Long wsId, @RequestParam("privateFlag") boolean privateFlag) {
		boolean result = likeService.insertLike(userIdx, wsId, privateFlag);
		
		if(!result) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(true);
	}
	
	/**
	 * 사용자가 명언에 대해 좋아요를 해제한다.
	 * @param userIdx
	 * @param wsId
	 * @param privateFlag
	 * @return
	 */
	@DeleteMapping
	public ResponseEntity<Boolean> deleteLike(@PathVariable("userIdx") Long userIdx, @PathVariable("wsId") Long wsId, @RequestParam("privateFlag") boolean privateFlag) {
		boolean result = likeService.deleteLike(userIdx, wsId, privateFlag);
		
		if(!result) {
			return ResponseEntity.notFound().build();
		}
		
		return new ResponseEntity<Boolean>(false, HttpStatus.NO_CONTENT);
	}
}