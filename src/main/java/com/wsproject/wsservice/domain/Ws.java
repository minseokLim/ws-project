package com.wsproject.wsservice.domain;

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

// TODO need to optimize size of each column
@Entity
@Getter
@NoArgsConstructor
@Table(name = "TBL_WS", indexes = {@Index(columnList = "ownerEmail")})
public class Ws extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String content;
	
	private String author;
	
	private WsType type;
	
	private boolean byAdmin;
	
	private String ownerEmail;
	
	@Builder
	public Ws(String content, String author, WsType type, boolean byAdmin, String ownerEmail) {
		this.content = content;
		this.author = author;
		this.type = type;
		this.byAdmin = byAdmin;
		this.ownerEmail = ownerEmail;
	}

	public void update(Ws ws) {
		this.content = ws.getContent();
		this.author = ws.getAuthor();
		this.type = ws.getType();
		this.byAdmin = ws.isByAdmin();
		this.ownerEmail = ws.getOwnerEmail();
	}
}
