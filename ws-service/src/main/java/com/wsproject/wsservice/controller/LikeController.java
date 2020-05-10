package com.wsproject.wsservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wsproject.wsservice.domain.Like;
import com.wsproject.wsservice.dto.LikeDto;
import com.wsproject.wsservice.service.LikeService;

import lombok.AllArgsConstructor;

/**
 * 좋아요 관련 Controller
 * @author mslim
 *
 */
@AllArgsConstructor
@RestController
@RequestMapping("/v1.0/users/{userIdx}/wses/like")
public class LikeController {

	private LikeService likeService;
	
	/**
	 * 사용자가 명언에 대해 좋아요 표시를 한다.
	 * @param userIdx
	 * @param likeDto
	 * @return
	 */
	@PostMapping
	public ResponseEntity<LikeDto> insertLike(@PathVariable("userIdx") Long userIdx, @RequestBody LikeDto likeDto) {
		Like result = likeService.insertLike(likeDto);
		
		return new ResponseEntity<LikeDto>(new LikeDto(result), HttpStatus.CREATED);
	}
	
	/**
	 * 사용자가 명언에 대해 좋아요를 해제한다.
	 * @param userIdx
	 * @param likeDto
	 * @return
	 */
	@DeleteMapping
	public ResponseEntity<Void> deleteLike(@PathVariable("userIdx") Long userIdx, @RequestBody LikeDto likeDto) {
		likeService.deleteLike(likeDto.toEntity().getLikeId());
		
		return ResponseEntity.noContent().build();
	}
}