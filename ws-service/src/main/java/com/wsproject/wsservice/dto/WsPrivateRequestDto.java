package com.wsproject.wsservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.wsproject.wsservice.domain.WsPrivate;
import com.wsproject.wsservice.domain.enums.WsType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 명언(사용자등록) Request DTO Class
 * @author mslim
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class WsPrivateRequestDto {

	@NotBlank(message = "content is required")
	private String content;
	
	@NotBlank(message = "author is required")
	private String author;
	
	@NotNull(message = "type is required")
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
