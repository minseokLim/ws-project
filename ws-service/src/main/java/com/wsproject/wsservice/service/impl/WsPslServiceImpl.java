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

import com.wsproject.wsservice.controller.WsPslController;
import com.wsproject.wsservice.domain.WsPsl;
import com.wsproject.wsservice.dto.WsPslDto;
import com.wsproject.wsservice.repository.WsPslRepository;
import com.wsproject.wsservice.service.WsPslService;
import com.wsproject.wsservice.util.CommonUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WsPslServiceImpl implements WsPslService {
		
	private WsPslRepository wsPslRepository;
	
	@Override
	public PagedModel<WsPslDto> selectWsPsls(Long ownerIdx, String search, Pageable pageable) {
		// 일단 search에 대한 부분은 미구현
		Page<WsPsl> page = wsPslRepository.findByOwnerIdx(ownerIdx, pageable);

		List<WsPslDto> content = page.stream().map(elem -> {
			WsPslDto dto = new WsPslDto(elem);
			CommonUtil.setLinkAdvice(dto, linkTo(methodOn(WsPslController.class).selectWsPersonal(ownerIdx, dto.getId())).withSelfRel());
			return dto;
		}).collect(Collectors.toList());
		
		PageMetadata pageMetadata = new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements());
		PagedModel<WsPslDto> result = new PagedModel<>(content, pageMetadata);
		CommonUtil.setLinkAdvice(result, linkTo(methodOn(WsPslController.class).selectWsPersonalPage(ownerIdx, search, pageable)).withSelfRel());
		CommonUtil.setPageLinksAdvice(result, page);
		
		return result;
	}

	@Override
	public WsPslDto selectWsPsl(Long ownerIdx, Long id) {
		Optional<WsPsl> data = wsPslRepository.findByIdAndOwnerIdx(id, ownerIdx);
		
		if(!data.isPresent()) {
			return null;
		}
		
		WsPslDto result = new WsPslDto(data.get());
		CommonUtil.setLinkAdvice(result, linkTo(methodOn(WsPslController.class).selectWsPersonal(result.getOwnerIdx(), result.getId())).withSelfRel());
		
		return result;
	}

	@Override
	public WsPslDto insertWsPsl(WsPslDto dto) {
		WsPsl wsPersonal = wsPslRepository.save(dto.toEntity());
				
		WsPslDto result = new WsPslDto(wsPersonal);
		CommonUtil.setLinkAdvice(result, linkTo(methodOn(WsPslController.class).selectWsPersonal(result.getOwnerIdx(), result.getId())).withSelfRel());
		
		return result;
	}

	@Override
	public WsPslDto updateWsPsl(Long ownerIdx, Long id, WsPslDto dto) {
		Optional<WsPsl> data = wsPslRepository.findByIdAndOwnerIdx(id, ownerIdx);
		
		if(!data.isPresent()) {
			return null;
		}
		
		WsPsl wsPersonal = data.get();
		wsPersonal.update(dto.toEntity());
		
		wsPersonal = wsPslRepository.save(wsPersonal);
		
		WsPslDto result = new WsPslDto(wsPersonal);
		CommonUtil.setLinkAdvice(result, linkTo(methodOn(WsPslController.class).selectWsPersonal(result.getOwnerIdx(), result.getId())).withSelfRel());
		
		return result;
	}

	@Override
	public boolean deleteWsPsl(Long ownerIdx, Long id) {
		Optional<WsPsl> data = wsPslRepository.findByIdAndOwnerIdx(id, ownerIdx);
		
		if(!data.isPresent()) {
			return false;
		}
		
		wsPslRepository.deleteById(id);
		return true;
	}
}
