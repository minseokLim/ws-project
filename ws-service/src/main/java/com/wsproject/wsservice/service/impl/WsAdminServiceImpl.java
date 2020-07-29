package com.wsproject.wsservice.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Predicate;
import com.wsproject.wsservice.domain.WsAdmin;
import com.wsproject.wsservice.domain.search.WsAdminSearch;
import com.wsproject.wsservice.dto.WsAdminRequestDto;
import com.wsproject.wsservice.dto.WsAdminResponseDto;
import com.wsproject.wsservice.repository.WsAdminRepository;
import com.wsproject.wsservice.service.WsAdminService;
import com.wsproject.wsservice.util.CommonUtil;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class WsAdminServiceImpl implements WsAdminService {
		
	private WsAdminRepository wsAdminRepository;
	
	@Override
	@Transactional(readOnly = true)
	public PagedModel<WsAdminResponseDto> selectWsAdminList(String search, Pageable pageable) {
		Page<WsAdmin> page;
		Predicate predicate = CommonUtil.extractSearchParameter(search, WsAdminSearch::getBooleanExpression);
		
		if(predicate == null) {
			page = wsAdminRepository.findAll(pageable);
		} else {
			page = wsAdminRepository.findAll(predicate, pageable);
		}
		
		PagedModel<WsAdminResponseDto> result = CommonUtil.setPageLinksAdvice(page, WsAdminResponseDto::new);
		
		return result;
	}
	
	@Override
	@Transactional(readOnly = true)
	public WsAdminResponseDto selectWsAdmin(Long id) {
		Optional<WsAdmin> data = wsAdminRepository.findById(id);
		
		if(!data.isPresent()) {
			return null;
		}
		
		return new WsAdminResponseDto(data.get());
	}

	@Override
	public WsAdminResponseDto insertWsAdmin(WsAdminRequestDto dto) {
		WsAdmin ws = wsAdminRepository.save(dto.toEntity());
		
		return new WsAdminResponseDto(ws);
	}

	@Override
	public WsAdminResponseDto updateWsAdmin(Long id, WsAdminRequestDto dto) {
		Optional<WsAdmin> data = wsAdminRepository.findById(id);
		
		if(!data.isPresent()) {
			return null;
		}
		
		WsAdmin ws = data.get();
		ws.update(dto.toEntity()); // 변경감지로 인한 업데이트
		
		return new WsAdminResponseDto(ws);
	}

	@Override
	public void deleteWsAdmin(Long id) {
		wsAdminRepository.deleteById(id);
	}
}
