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

/**
 * 명언(사용자 등록)
 * @author mslim
 *
 */
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
	
	public void update(WsPsl wsPsl) {
		this.content = wsPsl.getContent();
		this.author = wsPsl.getAuthor();
		this.type = wsPsl.getType();
		this.ownerIdx = wsPsl.getOwnerIdx();
	}
	
	public TodaysWs toTodaysWs() {
		return TodaysWs.builder().userIdx(ownerIdx).content(content).author(author).type(type).build();
	}
}
