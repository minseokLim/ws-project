package com.wsProject.wsService.dto;

import java.io.Serializable;

import com.wsProject.wsService.domain.enums.WS_Type;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WS_DTO implements Serializable {

	private static final long serialVersionUID = -8023948837054910029L;

	private Long id;
	
	private String content;
	
	private String author;
	
	private WS_Type type;
	
	private boolean byAdmin;
	
	private String ownerId;
	
	private int likeCnt;	
}
