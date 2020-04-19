package com.wsproject.wsservice.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.wsproject.wsservice.domain.enums.WsType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 오늘의 명언
 * @author mslim
 *
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "TBL_TODAYS_WS")
public class TodaysWs extends BaseTimeEntity {
	
	@Id
	private Long userIdx;
	
	private String content;
	
	private String author;
	
	private WsType type;
	
	@Builder
	public TodaysWs(Long userIdx, String content, String author, WsType type) {
		this.userIdx = userIdx;
		this.content = content;
		this.author = author;
		this.type = type;
	}
	
	public void update(TodaysWs todaysWs) {
		this.userIdx = todaysWs.getUserIdx();
		this.content = todaysWs.getContent();
		this.author = todaysWs.getAuthor();
		this.type = todaysWs.getType();
	}
}
