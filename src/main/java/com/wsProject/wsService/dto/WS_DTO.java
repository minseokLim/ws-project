package com.wsProject.wsService.dto;

import org.springframework.hateoas.RepresentationModel;

import com.wsProject.wsService.domain.WS;
import com.wsProject.wsService.domain.enums.WS_Type;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WS_DTO extends RepresentationModel<WS_DTO> {
	
	private Long id;
	
	private String content;
	
	private String author;
	
	private WS_Type type;
	
	private boolean byAdmin;
	
	private String ownerEmail;
	
	private boolean like;
	
	@Builder
	public WS_DTO(Long id, String content, String author, WS_Type type, boolean byAdmin, String ownerEmail, boolean like) {
		this.id = id;
		this.content = content;
		this.author = author;
		this.type = type;
		this.byAdmin = byAdmin;
		this.ownerEmail = ownerEmail;
		this.like = like;
	}
	
	public WS_DTO(WS ws) {
		this.id = ws.getId();
		this.content = ws.getContent();
		this.author = ws.getAuthor();
		this.type = ws.getType();
		this.byAdmin = ws.isByAdmin();
		this.ownerEmail = ws.getOwnerEmail();
		this.like = ws.isLike();
	}
}
