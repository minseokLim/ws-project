package com.mslimProject.wiseSayingService.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.mslimProject.wiseSayingService.domain.enums.WS_Type;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO need to optimize size of each column
// TODO foreign key?? ownerId? is it possible and reasonable?
@Entity
@Getter
@NoArgsConstructor
public class WS extends BaseTimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String content;
	
	private String author;
	
	private WS_Type type;
	
	private boolean byAdmin;
	
	private String ownerId;
	
	@Builder
	public WS(String content, String author, WS_Type type, boolean byAdmin, String ownerId) {
		this.content = content;
		this.author = author;
		this.type = type;
		this.byAdmin = byAdmin;
		this.ownerId = ownerId;
	}	
}
