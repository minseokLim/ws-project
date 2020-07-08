package com.wsproject.wsservice.service.impl;

import static com.wsproject.wsservice.domain.QWsPrivate.wsPrivate;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.wsproject.wsservice.domain.WsPrivate;
import com.wsproject.wsservice.domain.search.WsPrivateSearch;
import com.wsproject.wsservice.dto.WsPrivateRequestDto;
import com.wsproject.wsservice.dto.WsPrivateResponseDto;
import com.wsproject.wsservice.repository.WsPrivateRepository;
import com.wsproject.wsservice.service.WsPrivateService;
import com.wsproject.wsservice.util.CommonUtil;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class WsPrivateServiceImpl implements WsPrivateService {
		
	private WsPrivateRepository wsPrivateRepository;
	
	private WsPrivateSearch wsPrivateSearch;
	
	@Override
	@Transactional(readOnly = true)
	public PagedModel<WsPrivateResponseDto> selectWsPrivateList(Long ownerIdx, String search, Pageable pageable) {
		BooleanExpression predicate = CommonUtil.extractSearchParameter(search, wsPrivateSearch);
		BooleanExpression ownerIdxCondition = wsPrivate.ownerIdx.eq(ownerIdx);
		
		if(predicate == null) {
			predicate = ownerIdxCondition;
		} else {
			predicate = predicate.and(ownerIdxCondition);
		}
		
		Page<WsPrivate> page = wsPrivateRepository.findAll(predicate, pageable);
		
		PagedModel<WsPrivateResponseDto> result = CommonUtil.setPageLinksAdvice(page, WsPrivateResponseDto::new);
		
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public WsPrivateResponseDto selectWsPrivate(Long ownerIdx, Long id) {
		Optional<WsPrivate> data = wsPrivateRepository.findByIdAndOwnerIdx(id, ownerIdx);
		
		if(!data.isPresent()) {
			return null;
		}
		
		return new WsPrivateResponseDto(data.get());
	}

	@Override
	public WsPrivateResponseDto insertWsPrivate(WsPrivateRequestDto dto) {
		WsPrivate wsPsl = wsPrivateRepository.save(dto.toEntity());
				
		return new WsPrivateResponseDto(wsPsl);
	}

	@Override
	public WsPrivateResponseDto updateWsPrivate(Long ownerIdx, Long id, WsPrivateRequestDto dto) {
		Optional<WsPrivate> data = wsPrivateRepository.findByIdAndOwnerIdx(id, ownerIdx);
		
		if(!data.isPresent()) {
			return null;
		}
		
		WsPrivate wsPsl = data.get();
		wsPsl.update(dto); // 변경감지로 인한 업데이트
		
		return new WsPrivateResponseDto(wsPsl);
	}

	@Override
	public void deleteWsPrivate(Long ownerIdx, Long id) {
		wsPrivateRepository.deleteById(id);
	}
}
