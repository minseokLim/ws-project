package com.wsproject.wsservice.dto;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.wsproject.wsservice.domain.TodaysWs;
import com.wsproject.wsservice.domain.enums.WsType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 오늘의 명언 DTO class
 * @author mslim
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class TodaysWsDto extends RepresentationModel<TodaysWsDto> {
	
	private Long userIdx;
	
	private String content;
	
	private String author;
	
	private WsType type;
	
	private Long wsId; // 명언 ID
	
	private boolean psl; // 사용자가 등록한 명언인지의 여부
	
	private boolean liked; // 사용자의 좋아요 추가 여부
	
	private LocalDateTime createdDate;
	
	private LocalDateTime modifiedDate;
	
	public TodaysWsDto(TodaysWs ws) {
		this.userIdx = ws.getUserIdx();
		this.content = ws.getContent();
		this.author = ws.getAuthor();
		this.type = ws.getType();
		this.wsId = ws.getWsId();
		this.psl = ws.isPsl();
		this.createdDate = ws.getCreatedDate();
		this.modifiedDate = ws.getModifiedDate();
	}
	
	public TodaysWs toEntity() {
		return TodaysWs.builder().userIdx(userIdx).content(content).author(author).type(type).wsId(wsId).psl(psl).build();
	}
}
