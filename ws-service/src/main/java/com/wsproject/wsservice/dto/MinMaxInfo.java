package com.wsproject.wsservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MinMaxInfo {

	private Long wsMinId;
	private Long wsMaxId;
	private Long wsPslMinId;
	private Long wsPslMaxId;
	
	@Builder
	public MinMaxInfo(Long wsMinId, Long wsMaxId, Long wsPslMinId, Long wsPslMaxId) {
		super();
		this.wsMinId = wsMinId;
		this.wsMaxId = wsMaxId;
		this.wsPslMinId = wsPslMinId;
		this.wsPslMaxId = wsPslMaxId;
	}
}
