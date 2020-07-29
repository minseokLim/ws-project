package com.wsproject.wsservice.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.wsproject.wsservice.controller.WsAdminController;
import com.wsproject.wsservice.domain.WsAdmin;
import com.wsproject.wsservice.domain.enums.WsType;
import com.wsproject.wsservice.util.CommonUtil;

import lombok.Getter;
import lombok.ToString;

/**
 * 명언(관리자등록) Response DTO Class
 * @author mslim
 *
 */
@Getter
@ToString
public class WsAdminResponseDto extends RepresentationModel<WsAdminResponseDto> {
	
	private Long id;
	
	private String content;
	
	private String author;
	
	private WsType type;
		
	private LocalDateTime createdDate;
	
	private LocalDateTime modifiedDate;

	public WsAdminResponseDto(WsAdmin ws) {
		this.id = ws.getId();
		this.content = ws.getContent();
		this.author = ws.getAuthor();
		this.type = ws.getType();
		this.createdDate = ws.getCreatedDate();
		this.modifiedDate = ws.getModifiedDate();
		
		// HATEOAS Link 정보 추가
		CommonUtil.setLink(this, linkTo(WsAdminController.class).slash(this.id).withSelfRel());
	}
}
