package com.wsproject.wsservice.dto;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.wsproject.wsservice.domain.WsPersonal;
import com.wsproject.wsservice.domain.enums.WsType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WsPersonalDto extends RepresentationModel<WsPersonalDto> {
	
private Long id;
	
	private String content;
	
	private String author;
	
	private WsType type;
	
	private String ownerEmail;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime modifiedDate;

	public WsPersonalDto(WsPersonal wsPersonal) {
		this.id = wsPersonal.getId();
		this.content = wsPersonal.getContent();
		this.author = wsPersonal.getAuthor();
		this.type = wsPersonal.getType();
		this.ownerEmail = wsPersonal.getOwnerEmail();
		this.createdDate = wsPersonal.getCreatedDate();
		this.modifiedDate = wsPersonal.getModifiedDate();
	}
	
	public WsPersonal toEntity() {
		return WsPersonal.builder().content(content).author(author).type(type).ownerEmail(ownerEmail).build();
	}
}
