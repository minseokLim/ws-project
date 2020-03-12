package com.wsproject.wsservice.dto;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.wsproject.wsservice.domain.TodaysWs;
import com.wsproject.wsservice.domain.enums.WsType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TodaysWsDto extends RepresentationModel<TodaysWsDto> {
	
	private Long userIdx;
	
	private String content;
	
	private String author;
	
	private WsType type;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime modifiedDate;
	
	public TodaysWsDto(TodaysWs ws) {
		this.userIdx = ws.getUserIdx();
		this.content = ws.getContent();
		this.author = ws.getAuthor();
		this.type = ws.getType();
		this.createdDate = ws.getCreatedDate();
		this.modifiedDate = ws.getModifiedDate();
	}
	
	public TodaysWs toEntity() {
		return TodaysWs.builder().userIdx(userIdx).content(content).author(author).type(type).build();
	}
}
