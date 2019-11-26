package com.mslimProject.wiseSayingService.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//TODO need to optimize size of each column
@Entity
@Getter
@NoArgsConstructor
public class LikeCnt {
	
	@Id
	private String ownerId;
	
	@Id
	private Long wsId;
	
	private int likeCnt;
	
	@Builder
	public LikeCnt(String ownerId, Long wsId, int likeCnt) {
		this.ownerId = ownerId;
		this.wsId = wsId;
		this.likeCnt = likeCnt;
	}
}
