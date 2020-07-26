package com.wsproject.wsservice.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.wsproject.wsservice.domain.enums.WsType;
import com.wsproject.wsservice.dto.WsAdminRequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 명언(관리자 등록)
 * @author mslim
 *
 */
@Entity
@Getter
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@Table(name = "TBL_WS_ADMIN")
public class WsAdmin extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "WS_ADMIN_ID")
	private Long id;
	
	@Column(nullable = false)
	private String content; // 명언 내용
	
	@Column(nullable = false)
	private String author; // 말한 사람
	
	@Column(nullable = false)
	private WsType type; // 명언 종류
	
	@OneToMany(mappedBy = "ws", cascade = CascadeType.REMOVE)
	private List<WsAdminLike> likes = new ArrayList<WsAdminLike>();
	
	@OneToMany(mappedBy = "wsAdmin", cascade = CascadeType.REMOVE)
	private List<TodaysWs> todaysWsList = new ArrayList<TodaysWs>();
	
	@Version
	@Column(columnDefinition = "integer default 0", nullable = false)
	private Integer version;
	
	@Builder
	private WsAdmin(String content, String author, WsType type) {
		this.content = content;
		this.author = author;
		this.type = type;
	}

	public void update(WsAdmin ws) {
		this.content = ws.getContent() != null ? ws.getContent() : content;
		this.author = ws.getAuthor() != null ? ws.getAuthor() : author;
		this.type = ws.getType() != null ? ws.getType() : type;
	}
	
	public void update(WsAdminRequestDto dto) {
		update(dto.toEntity());
	}

	@Override
	public String toString() {
		return "WsAdmin [id=" + id + ", content=" + content + ", author=" + author + ", type=" + type + ", version=" + version + "]";
	}
}
