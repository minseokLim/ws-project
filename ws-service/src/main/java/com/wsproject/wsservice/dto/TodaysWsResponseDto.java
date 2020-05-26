package com.wsproject.wsservice.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.wsproject.wsservice.controller.TodaysWsController;
import com.wsproject.wsservice.domain.TodaysWs;
import com.wsproject.wsservice.domain.WsAdmin;
import com.wsproject.wsservice.domain.WsPrivate;
import com.wsproject.wsservice.domain.enums.WsType;
import com.wsproject.wsservice.repository.WsAdminLikeRepository;
import com.wsproject.wsservice.util.CommonUtil;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 오늘의 명언 Response DTO class
 * @author mslim
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class TodaysWsResponseDto extends RepresentationModel<TodaysWsResponseDto> {
	
	private Long id;
	
	private Long userIdx;
	
	private String content;
	
	private String author;
	
	private WsType type;
	
	private Long wsId; // 명언 ID
	
	private boolean privateFlag; // 사용자가 등록한 명언인지의 여부
	
	private boolean liked; // 사용자의 좋아요 추가 여부
	
	private LocalDateTime createdDate;
	
	private LocalDateTime modifiedDate;
	
	public TodaysWsResponseDto(TodaysWs todaysWs) {
		this.id = todaysWs.getId();
		this.userIdx = todaysWs.getUserIdx();
		
		if(todaysWs.getWsAdmin() != null) {
			WsAdmin wsAdmin = todaysWs.getWsAdmin();
			
			this.content = wsAdmin.getContent();
			this.author = wsAdmin.getAuthor();
			this.type = wsAdmin.getType();
			this.wsId = wsAdmin.getId();
			this.privateFlag = false;
			
			WsAdminLikeRepository wsAdminLikeRepository = CommonUtil.getBean(WsAdminLikeRepository.class);
			
			this.liked = wsAdminLikeRepository.findByWsAndUserIdx(wsAdmin, this.userIdx).isPresent();
		} else {
			WsPrivate wsPrivate = todaysWs.getWsPrivate();
			
			this.content = wsPrivate.getContent();
			this.author = wsPrivate.getAuthor();
			this.type = wsPrivate.getType();
			this.wsId = wsPrivate.getId();
			this.privateFlag = true;
			this.liked = wsPrivate.isLiked();
		}
		
		this.createdDate = todaysWs.getCreatedDate();
		this.modifiedDate = todaysWs.getModifiedDate();
		
		// HATEOAS Link 정보 추가
		CommonUtil.setLinkAdvice(this, linkTo(methodOn(TodaysWsController.class).selectTodaysWs(this.userIdx)).withSelfRel());
	}
}
