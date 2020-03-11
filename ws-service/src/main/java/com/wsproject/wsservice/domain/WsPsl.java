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

@Entity
@Getter
@NoArgsConstructor
@Table(name = "TBL_WS_PSL", indexes = {@Index(columnList = "ownerIdx")})
public class WsPsl extends BaseTimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String content;
	
	private String author;
	
	private WsType type;
	
	private Long ownerIdx;

	@Builder
	public WsPsl(String content, String author, WsType type, Long ownerIdx) {
		this.content = content;
		this.author = author;
		this.type = type;
		this.ownerIdx = ownerIdx;
	}
	
	public void update(WsPsl wsPersonal) {
		this.content = wsPersonal.getContent();
		this.author = wsPersonal.getAuthor();
		this.type = wsPersonal.getType();
		this.ownerIdx = wsPersonal.getOwnerIdx();
	}
}
