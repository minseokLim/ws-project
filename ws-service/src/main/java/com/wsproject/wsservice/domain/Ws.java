package com.wsproject.wsservice.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.wsproject.wsservice.domain.enums.WsType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "TBL_WS")
public class Ws extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String content;
	
	private String author;
	
	private WsType type;
	
	@Builder
	public Ws(String content, String author, WsType type) {
		this.content = content;
		this.author = author;
		this.type = type;
	}

	public void update(Ws ws) {
		this.content = ws.getContent();
		this.author = ws.getAuthor();
		this.type = ws.getType();
	}
}
