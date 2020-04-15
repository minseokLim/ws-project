package com.wsproject.wsservice.dto;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.wsproject.wsservice.domain.WsPsl;
import com.wsproject.wsservice.domain.enums.WsType;

import lombok.Getter;
import lombok.NoArgsConstructor;

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

	public WsPslDto(WsPsl wsPersonal) {
		this.id = wsPersonal.getId();
		this.content = wsPersonal.getContent();
		this.author = wsPersonal.getAuthor();
		this.type = wsPersonal.getType();
		this.ownerIdx = wsPersonal.getOwnerIdx();
		this.createdDate = wsPersonal.getCreatedDate();
		this.modifiedDate = wsPersonal.getModifiedDate();
	}
	
	public WsPsl toEntity() {
		return WsPsl.builder().content(content).author(author).type(type).ownerIdx(ownerIdx).build();
	}
}
