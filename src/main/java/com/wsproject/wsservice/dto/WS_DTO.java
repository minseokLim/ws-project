package com.wsproject.wsservice.dto;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.wsproject.wsservice.domain.WS;
import com.wsproject.wsservice.domain.enums.WS_Type;

import lombok.Getter;

@Getter
public class WS_DTO extends RepresentationModel<WS_DTO> {
	
	private Long id;
	
	private String content;
	
	private String author;
	
	private WS_Type type;
	
	private boolean byAdmin;
	
	private String ownerEmail;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime modifiedDate;

	public WS_DTO(WS ws) {
		this.id = ws.getId();
		this.content = ws.getContent();
		this.author = ws.getAuthor();
		this.type = ws.getType();
		this.byAdmin = ws.isByAdmin();
		this.ownerEmail = ws.getOwnerEmail();
		this.createdDate = ws.getCreatedDate();
		this.modifiedDate = ws.getModifiedDate();
	}
}
