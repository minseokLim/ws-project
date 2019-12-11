package com.wsproject.wsservice.dto;

import org.springframework.hateoas.RepresentationModel;

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
	
	private boolean like;
}
