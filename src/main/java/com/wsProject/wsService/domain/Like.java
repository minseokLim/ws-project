package com.wsProject.wsService.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//TODO need to optimize size of each column
@Entity
@Getter
@NoArgsConstructor
@Table(name = "TBL_LIKE")
public class Like implements Serializable {
	
	private static final long serialVersionUID = -6018555674117682748L;

	@Id
	@Column(length = 100)
	private String userEmail;
	
	@Id
	@OneToOne
	private WS ws;
	
	@Builder
	public Like(String userEmail, WS ws) {
		this.userEmail = userEmail;
		this.ws = ws;
	}	
}
