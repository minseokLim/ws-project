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

import com.wsproject.wsservice.controller.WsPrivateController;
import com.wsproject.wsservice.domain.WsPrivate;
import com.wsproject.wsservice.domain.enums.WsType;
import com.wsproject.wsservice.dto.WsPrivateRequestDto;
import com.wsproject.wsservice.dto.WsPrivateResponseDto;
import com.wsproject.wsservice.repository.WsPrivateRepository;
import com.wsproject.wsservice.service.WsPrivateService;
import com.wsproject.wsservice.util.CommonUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WsPrivateServiceImpl implements WsPrivateService {
		
	private WsPrivateRepository wsPrivateRepository;
	
	@Override
	public PagedModel<WsPrivateResponseDto> selectWsPrivateList(Long ownerIdx, String search, Pageable pageable) {
		Page<WsPrivate> page;
		
		if(search == null || "".equals(search)) {
			page = wsPrivateRepository.findByOwnerIdx(ownerIdx, pageable);
		} else {
			String[] keyValue = CommonUtil.extractSearchParameter(search);
			String key = keyValue[0];
			String value = keyValue[1];
			
			switch (key) {
			case "content":
				page = wsPrivateRepository.findByOwnerIdxAndContentContaining(ownerIdx, value, pageable);
				break;
			case "author":
				page = wsPrivateRepository.findByOwnerIdxAndAuthorContaining(ownerIdx, value, pageable);
				break;
			case "type":
				page = wsPrivateRepository.findByOwnerIdxAndType(ownerIdx, WsType.valueOf(value), pageable);
				break;
			default:
				return null;
			}
		}
		
		PagedModel<WsPrivateResponseDto> result = setPageLinksAdvice(ownerIdx, search, pageable, page);
		
		return result;
	}

	/**
	 * HATEOAS Link 정보 추가
	 * @param ownerIdx
	 * @param search
	 * @param pageable
	 * @param page
	 * @return
	 */
	private PagedModel<WsPrivateResponseDto> setPageLinksAdvice(Long ownerIdx, String search, Pageable pageable, Page<WsPrivate> page) {
		List<WsPrivateResponseDto> content = page.stream().map(elem -> new WsPrivateResponseDto(elem)).collect(Collectors.toList());
		
		PageMetadata pageMetadata = new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements());
		PagedModel<WsPrivateResponseDto> result = new PagedModel<>(content, pageMetadata);
		CommonUtil.setLinkAdvice(result, linkTo(methodOn(WsPrivateController.class).selectWsPrivateList(ownerIdx, search, pageable)).withSelfRel());
		CommonUtil.setPageLinksAdvice(result, page);
		
		return result;
	}

	@Override
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
		wsPsl.update(dto);
		
		// TODO JPA 변경 감지 기능으로 이거 뺴도 되려나??;;
		wsPsl = wsPrivateRepository.save(wsPsl);
		
		return new WsPrivateResponseDto(wsPsl);
	}

	@Override
	public void deleteWsPrivate(Long ownerIdx, Long id) {
		wsPrivateRepository.deleteById(id);
	}
}
