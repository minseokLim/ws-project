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

import com.wsproject.wsservice.controller.WsAdminController;
import com.wsproject.wsservice.domain.WsAdmin;
import com.wsproject.wsservice.domain.enums.WsType;
import com.wsproject.wsservice.dto.WsAdminRequestDto;
import com.wsproject.wsservice.dto.WsAdminResponseDto;
import com.wsproject.wsservice.repository.WsAdminRepository;
import com.wsproject.wsservice.service.WsAdminService;
import com.wsproject.wsservice.util.CommonUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WsAdminServiceImpl implements WsAdminService {
		
	private WsAdminRepository wsAdminRepository;
		
	@Override
	public PagedModel<WsAdminResponseDto> selectWsAdminList(String search, Pageable pageable) {
		Page<WsAdmin> page;
		
		if(search == null || "".equals(search)) {
			page = wsAdminRepository.findAll(pageable);
		} else {
			String[] keyValue = CommonUtil.extractSearchParameter(search);
			String key = keyValue[0];
			String value = keyValue[1];
			
			switch (key) {
			case "content":
				page = wsAdminRepository.findByContentContaining(value, pageable);
				break;
			case "author":
				page = wsAdminRepository.findByAuthorContaining(value, pageable);
				break;
			case "type":
				page = wsAdminRepository.findByType(WsType.valueOf(value), pageable);
				break;
			default:
				return null;
			}
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
	private PagedModel<WsAdminResponseDto> setPageLinksAdvice(String search, Pageable pageable, Page<WsAdmin> page) {
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
		ws.update(dto);
		
		// TODO JPA 변경 감지 기능으로 이거 뺴도 되려나??;;
		ws = wsAdminRepository.save(ws);
		
		return new WsAdminResponseDto(ws);
	}

	@Override
	public void deleteWsAdmin(Long id) {
		wsAdminRepository.deleteById(id);
	}
}
