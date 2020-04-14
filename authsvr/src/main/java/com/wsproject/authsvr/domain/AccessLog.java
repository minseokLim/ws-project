package com.wsproject.authsvr.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "TBL_ACCESS_LOG")
public class AccessLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;
	
	private Long userIdx;
	
	private String ip;
	
	private LocalDateTime accessDate;

	@Builder
	public AccessLog(Long userIdx, String ip) {
		this.userIdx = userIdx;
		this.ip = ip;
	}
	
	public AccessLog setNowOnAccessDate() {
		this.accessDate = LocalDateTime.now();
		return this;
	}
}
