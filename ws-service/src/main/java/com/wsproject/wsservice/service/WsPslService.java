package com.wsproject.wsservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import com.wsproject.wsservice.dto.WsPslDto;

public interface WsPslService {
	public PagedModel<WsPslDto> selectWsPslList(Long ownerIdx, String search, Pageable pageable);
	public WsPslDto selectWsPsl(Long ownerIdx, Long id);
	public WsPslDto insertWsPsl(WsPslDto dto);
	public WsPslDto updateWsPsl(Long ownerIdx, Long id, WsPslDto dto);
	public boolean deleteWsPsl(Long ownerIdx, Long id);
}
