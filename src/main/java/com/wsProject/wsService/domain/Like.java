package com.wsProject.wsService.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//TODO need to optimize size of each column
@Entity
@Getter
@NoArgsConstructor
public class Like implements Serializable {
	
	private static final long serialVersionUID = 5254404325738810327L;

	@Id
	@Column(length = 50)
	private String userEmail;
	
	@Id
	private Long wsId;
		
	@Builder
	public Like(String userEmail, Long wsId) {
		this.userEmail = userEmail;
		this.wsId = wsId;
	}
}
