package com.wsproject.wsservice.dto;

import com.wsproject.wsservice.domain.WsPrivate;
import com.wsproject.wsservice.domain.enums.WsType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 명언(사용자등록) Request DTO Class
 * @author mslim
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class WsPrivateRequestDto {

	private String content;
	
	private String author;
	
	private WsType type;
	
	private Long ownerIdx;
	
	public WsPrivate toEntity() {
		return WsPrivate.builder().content(content).author(author).type(type).ownerIdx(ownerIdx).build();
	}

	@Builder
	private WsPrivateRequestDto(String content, String author, WsType type, Long ownerIdx) {
		this.content = content;
		this.author = author;
		this.type = type;
		this.ownerIdx = ownerIdx;
	}
}
