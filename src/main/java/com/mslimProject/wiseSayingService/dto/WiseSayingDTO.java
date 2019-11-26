package com.mslimProject.wiseSayingService.dto;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.mslimProject.wiseSayingService.domain.BaseTimeEntity;
import com.mslimProject.wiseSayingService.domain.enums.WS_Type;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WiseSayingDTO implements Serializable {

	private static final long serialVersionUID = -8023948837054910029L;

	private Long id;
	
	private String content;
	
	private String author;
	
	private WS_Type type;
	
	private boolean byAdmin;
	
	private String ownerId;
	
	private int likeCnt;
	
	
}
