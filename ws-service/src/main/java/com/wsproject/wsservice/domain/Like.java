package com.wsproject.wsservice.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 명언에 대한 사용자의 좋아요 여부
 * @author mslim
 *
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "TBL_LIKE")
public class Like extends BaseTimeEntity {
	
	@EmbeddedId
	private LikeId likeId;

	@Builder
	public Like(LikeId likeId) {
		super();
		this.likeId = likeId;
	}
}
