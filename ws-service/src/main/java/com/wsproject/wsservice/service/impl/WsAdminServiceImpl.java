package com.wsproject.wsservice.service.impl;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Predicate;
import com.wsproject.wsservice.controller.WsAdminController;
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
	
	private WsAdminSearch wsAdminSearch;
	
	@Override
	public PagedModel<WsAdminResponseDto> selectWsAdminList(String search, Pageable pageable) {
		Page<WsAdmin> page;
		Predicate predicate = CommonUtil.extractSearchParameter(search, wsAdminSearch);
		
		if(predicate == null) {
			page = wsAdminRepository.findAll(pageable);
		} else {
			page = wsAdminRepository.findAll(predicate, pageable);
		}
		
		PagedModel<WsAdminResponseDto> result = setPageLinksAdvice(search, pageable, page);
		
		return result;
	}

	/**
	 * HATEOAS Link 정보 추가
	 * @param search
	 * @param pageable
	 * @param page
	 * @return
	 */
	private static PagedModel<WsAdminResponseDto> setPageLinksAdvice(String search, Pageable pageable, Page<WsAdmin> page) {
		List<WsAdminResponseDto> content = page.stream().map(elem -> new WsAdminResponseDto(elem)).collect(Collectors.toList());
		
		PageMetadata pageMetadata = new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements());
		PagedModel<WsAdminResponseDto> result = new PagedModel<>(content, pageMetadata);
		CommonUtil.setLinkAdvice(result, linkTo(methodOn(WsAdminController.class).selectWsAdminList(search, pageable)).withSelfRel());
		CommonUtil.setPageLinksAdvice(result, page);
		
		return result;
	}
	
	@Override
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
		ws.update(dto); // 변경감지로 인한 업데이트
		
		return new WsAdminResponseDto(ws);
	}

	@Override
	public void deleteWsAdmin(Long id) {
		wsAdminRepository.deleteById(id);
	}
}
