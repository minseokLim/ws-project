package com.wsproject.wsservice.dto;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.wsproject.wsservice.domain.Ws;
import com.wsproject.wsservice.domain.enums.WsType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WsDto extends RepresentationModel<WsDto> {
	
	private Long id;
	
	private String content;
	
	private String author;
	
	private WsType type;
	
	private boolean byAdmin;
	
	private String ownerEmail;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime modifiedDate;

	public WsDto(Ws ws) {
		this.id = ws.getId();
		this.content = ws.getContent();
		this.author = ws.getAuthor();
		this.type = ws.getType();
		this.byAdmin = ws.isByAdmin();
		this.ownerEmail = ws.getOwnerEmail();
		this.createdDate = ws.getCreatedDate();
		this.modifiedDate = ws.getModifiedDate();
	}
	
	public Ws toEntity() {
		return Ws.builder().content(content).author(author).type(type).byAdmin(byAdmin).ownerEmail(ownerEmail).build();
	}
}
