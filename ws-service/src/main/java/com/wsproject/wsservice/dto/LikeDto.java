package com.wsproject.wsservice.dto;

import java.time.LocalDateTime;

import com.wsproject.wsservice.domain.Like;
import com.wsproject.wsservice.domain.LikeId;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 좋아요 Dto Class
 * @author mslim
 *
 */
@Getter
@NoArgsConstructor
public class LikeDto {
	private Long wsId; // 명언 ID
	
	private boolean psl; // 사용자가 등록한 명언인지의 여부
	
	private Long userIdx; // 사용자 Key Value
	
	private LocalDateTime createdDate;
	
	private LocalDateTime modifiedDate;
	
	public LikeDto(Like like) {
		this.wsId = like.getLikeId().getWsId();
		this.psl = like.getLikeId().isPsl();
		this.userIdx = like.getLikeId().getUserIdx();
		this.createdDate = like.getCreatedDate();
		this.modifiedDate = like.getModifiedDate();
	}
	
	public Like toEntity() {
		return Like.builder().likeId(LikeId.builder().wsId(wsId).psl(psl).userIdx(userIdx).build()).build();
	}
}
