package com.wsproject.wsservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.wsproject.wsservice.domain.enums.WsType;
import com.wsproject.wsservice.dto.WsPrivateRequestDto;

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
@Table(name = "TBL_WS_PRIVATE", indexes = {@Index(columnList = "OWNER_IDX")})
public class WsPrivate extends BaseTimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "WS_PRIVATE_ID")
	private Long id;
	
	@Column(nullable = false)
	private String content; // 명언 내용
	
	@Column(nullable = false)
	private String author; // 말한 사람
	
	private WsType type; // 명언 종류
	
	@Column(name = "OWNER_IDX", nullable = false)
	private Long ownerIdx;

	private boolean liked; // 사용자가 좋아요를 눌렀는지의 여부
	
	@Builder
	public WsPrivate(String content, String author, WsType type, Long ownerIdx) {
		this.content = content;
		this.author = author;
		this.type = type;
		this.ownerIdx = ownerIdx;
	}
	
	public void update(WsPrivate ws) {
		this.content = ws.getContent() != null ? ws.getContent() : content;
		this.author = ws.getAuthor() != null ? ws.getAuthor() : author;
		this.type = ws.getType() != null ? ws.getType() : type;
		this.ownerIdx = ws.getOwnerIdx() != null ? ws.getOwnerIdx() : ownerIdx;
	}
	
	public void update(WsPrivateRequestDto dto) {
		update(dto.toEntity());
	}
	
	public void setLiked(boolean liked) {
		this.liked = liked;
	}
}