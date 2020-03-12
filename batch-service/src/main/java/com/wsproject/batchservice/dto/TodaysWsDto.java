package com.wsproject.batchservice.dto;

import java.time.LocalDateTime;

import com.wsproject.batchservice.domain.enums.WsType;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TodaysWsDto {
	
	private Long userIdx;
	
	private String content;
	
	private String author;
	
	private WsType type;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime modifiedDate;

	@Builder
	public TodaysWsDto(Long userIdx, String content, String author, WsType type, LocalDateTime createdDate,
			LocalDateTime modifiedDate) {
		this.userIdx = userIdx;
		this.content = content;
		this.author = author;
		this.type = type;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}	
}
