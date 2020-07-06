package com.wsproject.wsservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 명언(관리자 등록)에 대한 사용자의 좋아요 여부 <br>
 * 이 값이 존재하면 사용자가 해당 명언에 좋아요를 누른 것이고, 존재하지 않으면 누르지 않은 것이다.
 * @author mslim
 *
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "TBL_WS_ADMIN_LIKE", uniqueConstraints = @UniqueConstraint(columnNames = {"WS_ADMIN_ID", "USER_IDX"}))
public class WsAdminLike {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LIKE_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "WS_ADMIN_ID")
	private WsAdmin ws; // 명언
	
	@Column(name = "USER_IDX")
	private Long userIdx; // 사용자의 Key value

	@Builder
	private WsAdminLike(WsAdmin ws, Long userIdx) {
		this.ws = ws;
		this.userIdx = userIdx;
	}
}