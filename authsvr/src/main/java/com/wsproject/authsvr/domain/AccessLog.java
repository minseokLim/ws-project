package com.wsproject.authsvr.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Access Log 정보를 담는 class
 * @author mslim
 *
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "TBL_ACCESS_LOG")
public class AccessLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;
	
	@Column(nullable = false)
	private Long userIdx;
	
	@Column(nullable = false)
	private String ip;
	
	@Column(nullable = false)
	private LocalDateTime accessDate;

	@Builder
	private AccessLog(Long userIdx, String ip) {
		this.userIdx = userIdx;
		this.ip = ip;
	}
	
	public AccessLog setNowOnAccessDate() {
		this.accessDate = LocalDateTime.now();
		return this;
	}

	@Override
	public String toString() {
		return "AccessLog [idx=" + idx + ", userIdx=" + userIdx + ", ip=" + ip + ", accessDate=" + accessDate + "]";
	}
}
