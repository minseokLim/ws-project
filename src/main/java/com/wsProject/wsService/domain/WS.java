package com.wsProject.wsService.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wsProject.wsService.domain.enums.WS_Type;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO need to optimize size of each column
@Entity
@Getter
@NoArgsConstructor
@Table(name = "TBL_WS")
public class WS extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String content;
	
	private String author;
	
	private WS_Type type;
	
	private boolean byAdmin;
	
	@Column(length = 100)
	private String ownerEmail;
	
	@Transient
	private boolean like;
	
	@Builder
	public WS(Long id, String content, String author, WS_Type type, boolean byAdmin, String ownerEmail, boolean like) {
		this.id = id;
		this.content = content;
		this.author = author;
		this.type = type;
		this.byAdmin = byAdmin;
		this.ownerEmail = ownerEmail;
		this.like = like;
	}
}
