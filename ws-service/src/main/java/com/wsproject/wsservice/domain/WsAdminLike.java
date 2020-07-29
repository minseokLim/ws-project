package com.wsproject.wsservice.domain;

import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.AccessLevel;
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
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_WS_ADMIN_LIKE", uniqueConstraints = @UniqueConstraint(columnNames = {"WS_ADMIN_ID", "USER_IDX"}))
public class WsAdminLike {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LIKE_ID")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WS_ADMIN_ID", nullable = false)
	private WsAdmin ws; // 명언
	
	@Column(name = "USER_IDX", nullable = false)
	private Long userIdx; // 사용자의 Key value

	@Builder
	private WsAdminLike(WsAdmin ws, Long userIdx) {
		this.ws = Objects.requireNonNull(ws);
		this.userIdx = Objects.requireNonNull(userIdx);
	}

	@Override
	public String toString() {
		return "WsAdminLike [id=" + id + ", ws=" + ws + ", userIdx=" + userIdx + "]";
	}
}