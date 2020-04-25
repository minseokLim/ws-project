package com.wsproject.wsservice.dto;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.wsproject.wsservice.domain.WsPsl;
import com.wsproject.wsservice.domain.enums.WsType;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 명언(사용자등록) DTO Class
 * @author mslim
 *
 */
@Getter
@NoArgsConstructor
public class WsPslDto extends RepresentationModel<WsPslDto> {
	
	private Long id;
	
	private String content;
	
	private String author;
	
	private WsType type;
	
	private Long ownerIdx;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime modifiedDate;

	public WsPslDto(WsPsl wsPsl) {
		this.id = wsPsl.getId();
		this.content = wsPsl.getContent();
		this.author = wsPsl.getAuthor();
		this.type = wsPsl.getType();
		this.ownerIdx = wsPsl.getOwnerIdx();
		this.createdDate = wsPsl.getCreatedDate();
		this.modifiedDate = wsPsl.getModifiedDate();
	}
	
	public WsPsl toEntity() {
		return WsPsl.builder().content(content).author(author).type(type).ownerIdx(ownerIdx).build();
	}
}
