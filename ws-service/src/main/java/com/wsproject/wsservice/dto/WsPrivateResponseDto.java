package com.wsproject.wsservice.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.wsproject.wsservice.controller.WsPrivateController;
import com.wsproject.wsservice.domain.WsPrivate;
import com.wsproject.wsservice.domain.enums.WsType;
import com.wsproject.wsservice.util.CommonUtil;

import lombok.Getter;

/**
 * 명언(사용자등록) Response DTO Class
 * @author mslim
 *
 */
@Getter
public class WsPrivateResponseDto extends RepresentationModel<WsPrivateResponseDto> {
	
	private Long id;
	
	private String content;
	
	private String author;
	
	private WsType type;
	
	private Long ownerIdx;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime modifiedDate;

	public WsPrivateResponseDto(WsPrivate wsPsl) {
		this.id = wsPsl.getId();
		this.content = wsPsl.getContent();
		this.author = wsPsl.getAuthor();
		this.type = wsPsl.getType();
		this.ownerIdx = wsPsl.getOwnerIdx();
		this.createdDate = wsPsl.getCreatedDate();
		this.modifiedDate = wsPsl.getModifiedDate();
		
		// HATEOAS Link 정보 추가
		CommonUtil.setLinkAdvice(this, linkTo(methodOn(WsPrivateController.class).selectWsPrivate(this.ownerIdx, this.id)).withSelfRel());
	}
}
