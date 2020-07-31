package com.wsproject.wsservice.service.impl;

import static com.wsproject.wsservice.util.CommonUtil.extractSearchParameter;
import static com.wsproject.wsservice.util.CommonUtil.setPageLinksAdvice;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.wsproject.wsservice.domain.WsAdmin;
import com.wsproject.wsservice.domain.search.WsAdminSearch;
import com.wsproject.wsservice.dto.WsAdminRequestDto;
import com.wsproject.wsservice.dto.WsAdminResponseDto;
import com.wsproject.wsservice.repository.WsAdminRepository;
import com.wsproject.wsservice.service.WsAdminService;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class WsAdminServiceImpl implements WsAdminService {
		
	private WsAdminRepository wsAdminRepository;
	
	@Override
	@Transactional(readOnly = true)
	public PagedModel<WsAdminResponseDto> selectWsAdminList(String search, Pageable pageable) {
		Optional<BooleanExpression> predicate = extractSearchParameter(search, WsAdminSearch::getBooleanExpression);
		Page<WsAdmin> page = predicate.map(expression -> wsAdminRepository.findAll(expression, pageable))
									  .orElseGet(() -> wsAdminRepository.findAll(pageable));

		PagedModel<WsAdminResponseDto> result = setPageLinksAdvice(page, WsAdminResponseDto::new);
		
		return result;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Optional<WsAdminResponseDto> selectWsAdmin(Long id) {
		Optional<WsAdmin> data = wsAdminRepository.findById(id);
		return data.map(ws -> Optional.of(new WsAdminResponseDto(ws))).orElseGet(Optional::empty);
	}

	@Override
	public WsAdminResponseDto insertWsAdmin(WsAdminRequestDto dto) {
		WsAdmin ws = wsAdminRepository.save(dto.toEntity());
		
		return new WsAdminResponseDto(ws);
	}

	@Override
	public Optional<WsAdminResponseDto> updateWsAdmin(Long id, WsAdminRequestDto dto) {
		Optional<WsAdmin> data = wsAdminRepository.findById(id);
		
		if(!data.isPresent()) {
			return Optional.empty();
		}
		
		WsAdmin ws = data.get().update(dto.toEntity()); // 변경감지로 인한 업데이트
		
		return Optional.of(new WsAdminResponseDto(ws));
	}

	@Override
	public void deleteWsAdmin(Long id) {
		wsAdminRepository.deleteById(id);
	}
}
