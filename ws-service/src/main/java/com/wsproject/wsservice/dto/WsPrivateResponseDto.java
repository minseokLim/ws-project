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
	
	private boolean liked;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime modifiedDate;

	public WsPrivateResponseDto(WsPrivate ws) {
		this.id = ws.getId();
		this.content = ws.getContent();
		this.author = ws.getAuthor();
		this.type = ws.getType();
		this.ownerIdx = ws.getOwnerIdx();
		this.liked = ws.isLiked();
		this.createdDate = ws.getCreatedDate();
		this.modifiedDate = ws.getModifiedDate();
		
		// HATEOAS Link 정보 추가
		CommonUtil.setLinkAdvice(this, linkTo(methodOn(WsPrivateController.class).selectWsPrivate(this.ownerIdx, this.id)).withSelfRel());
	}
}
