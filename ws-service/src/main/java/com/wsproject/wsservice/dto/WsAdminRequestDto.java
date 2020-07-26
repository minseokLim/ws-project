package com.wsproject.wsservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.wsproject.wsservice.domain.WsAdmin;
import com.wsproject.wsservice.domain.enums.WsType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 명언(관리자등록) Request DTO Class
 * @author mslim
 *
 */
@Getter
@ToString
@NoArgsConstructor
public class WsAdminRequestDto {
	
	@NotBlank(message = "content is required")
	private String content;
	
	@NotBlank(message = "author is required")
	private String author;
	
	@NotNull(message = "type is required")
	private WsType type;
	
	public WsAdmin toEntity() {
		return WsAdmin.builder().content(content).author(author).type(type).build();
	}

	@Builder
	private WsAdminRequestDto(String content, String author, WsType type) {
		this.content = content;
		this.author = author;
		this.type = type;
	}
}
