package com.wsproject.wsservice.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 좋아요(Like class)에 대한 복합키
 * @author mslim
 *
 */
@Embeddable
@Getter
@NoArgsConstructor
public class LikeId implements Serializable {

	private static final long serialVersionUID = -1628791003459741890L;
	
	private Long wsId; // 명언 ID
	
	private boolean psl; // 사용자가 등록한 명언인지의 여부
	
	private Long userIdx; // 사용자 Key Value

	@Builder
	public LikeId(Long wsId, boolean psl, Long userIdx) {
		this.wsId = wsId;
		this.psl = psl;
		this.userIdx = userIdx;
	}
}
