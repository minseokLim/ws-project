package com.mslimProject.wiseSayingService.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//TODO need to optimize size of each column
@Entity
@Getter
@NoArgsConstructor
public class LikeCnt implements Serializable {
	
	private static final long serialVersionUID = 5254404325738810327L;

	@Id
	private String userId;
	
	@Id
	private Long wsId;
	
	private int likeCnt;
	
	@Builder
	public LikeCnt(String userId, Long wsId, int likeCnt) {
		this.userId = userId;
		this.wsId = wsId;
		this.likeCnt = likeCnt;
	}
}
