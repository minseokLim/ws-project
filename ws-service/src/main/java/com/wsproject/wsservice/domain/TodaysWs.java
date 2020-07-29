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
import org.springframework.util.Assert;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 오늘의 명언
 * @author mslim
 *
 */
@Entity
@Getter
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_TODAYS_WS", uniqueConstraints = @UniqueConstraint(columnNames = {"USER_IDX"}))
public class TodaysWs extends BaseTimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TODAYS_WS_ID")
	private Long id;
	
	// 현재는 USER_IDX를 PK로 잡아도 관계없으나, 요구사항은 언제든지 바뀔 수 있기 때문에 
	// 한 사용자가 여러 개의 오늘의 명언을 가지게 되는 상황이 왔을 때를 대비
	@Column(name = "USER_IDX", nullable = false)
	private Long userIdx; // 사용자의 Key Value
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WS_ADMIN_ID")
	private WsAdmin wsAdmin; // 명언(관리자 등록)
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WS_PRIVATE_ID")
	private WsPrivate wsPrivate; // 명언(사용자 등록)
	
	@Builder
	private TodaysWs(Long userIdx, WsAdmin wsAdmin, WsPrivate wsPrivate) {
		this.userIdx = Objects.requireNonNull(userIdx);
		this.wsAdmin = wsAdmin;
		this.wsPrivate = wsPrivate;
		validate();
	}
	
	public TodaysWs update(TodaysWs todaysWs) {
		todaysWs.validate();
		
		this.userIdx = todaysWs.getUserIdx() != null ? todaysWs.getUserIdx() : userIdx;
		
		if(todaysWs.getWsAdmin() != null) {
			this.wsAdmin = todaysWs.getWsAdmin();
			this.wsPrivate = null;
		} else {
			this.wsPrivate = todaysWs.getWsPrivate();
			this.wsAdmin = null;
		}
		
		validate();
		
		return this;
	}

	@Override
	public String toString() {
		return "TodaysWs [id=" + id + ", userIdx=" + userIdx + ", wsAdmin=" + wsAdmin + ", wsPrivate=" + wsPrivate + "]";
	}
	
	/**
	 * 유효성 검사 <br>
	 * 오늘의 명언은 wsAdmin, wsPrivate 둘 중 하나만 가지고 있어야한다.
	 * @param wsAdmin
	 * @param wsPrivate
	 */
	private void validate() {
		Assert.isTrue((wsAdmin != null && wsPrivate == null) || (wsAdmin == null && wsPrivate != null), 
					  "TodaysWs must have only one NonNull value between wsAdmin and wsPrivate");
	}
}
