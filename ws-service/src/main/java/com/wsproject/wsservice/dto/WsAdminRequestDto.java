package com.wsproject.wsservice.dto;

import com.wsproject.wsservice.domain.WsAdmin;
import com.wsproject.wsservice.domain.enums.WsType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WsAdminRequestDto {
	
	private String content;
	
	private String author;
	
	private WsType type;
	
	public WsAdmin toEntity() {
		return WsAdmin.builder().content(content).author(author).type(type).build();
	}

	@Builder
	public WsAdminRequestDto(String content, String author, WsType type) {
		this.content = content;
		this.author = author;
		this.type = type;
	}
}
