package com.wsproject.wsservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.wsproject.wsservice.domain.enums.WsType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "TBL_WS_PERSONAL", indexes = {@Index(columnList = "ownerEmail")})
public class WsPersonal extends BaseTimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String content;
	
	private String author;
	
	private WsType type;
	
	@Column(length = 100)
	private String ownerEmail;

	@Builder
	public WsPersonal(String content, String author, WsType type, String ownerEmail) {
		this.content = content;
		this.author = author;
		this.type = type;
		this.ownerEmail = ownerEmail;
	}
	
	public void update(WsPersonal wsPersonal) {
		this.content = wsPersonal.getContent();
		this.author = wsPersonal.getAuthor();
		this.type = wsPersonal.getType();
		this.ownerEmail = wsPersonal.getOwnerEmail();
	}
}
