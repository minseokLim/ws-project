package com.wsProject.wsService.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.wsProject.wsService.domain.enums.WS_Type;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO need to optimize size of each column
@Entity
@Getter
@NoArgsConstructor
public class WS extends BaseTimeEntity implements Serializable {

	private static final long serialVersionUID = -8023948837054910029L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String content;
	
	private String author;
	
	private WS_Type type;
	
	private boolean byAdmin;
	
	@Column(length = 50)
	private String ownerEmail;
	
	@Builder
	public WS(String content, String author, WS_Type type, boolean byAdmin, String ownerEmail) {
		this.content = content;
		this.author = author;
		this.type = type;
		this.byAdmin = byAdmin;
		this.ownerEmail = ownerEmail;
	}	
}
