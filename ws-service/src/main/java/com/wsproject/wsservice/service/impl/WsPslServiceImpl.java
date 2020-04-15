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
import com.wsproject.wsservice.repository.TodaysWsRepository;
import com.wsproject.wsservice.repository.WsPslRepository;
import com.wsproject.wsservice.service.WsPslService;
import com.wsproject.wsservice.util.CommonUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WsPslServiceImpl implements WsPslService {
		
	private WsPslRepository wsPslRepository;
	
	private TodaysWsRepository todaysWsRepository;
	
	private CommonUtil commonUtil;
	
	@Override
	public PagedModel<WsPslDto> selectWsPsls(Long ownerIdx, String search, Pageable pageable) {
		// 일단 search에 대한 부분은 미구현
		Page<WsPsl> page = wsPslRepository.findByOwnerIdx(ownerIdx, pageable);

		List<WsPslDto> content = page.stream().map(elem -> {
			WsPslDto dto = new WsPslDto(elem);
			dto.add(linkTo(methodOn(WsPslController.class).selectWsPersonal(ownerIdx, dto.getId())).withSelfRel());
			return dto;
		}).collect(Collectors.toList());
		
		PageMetadata pageMetadata = new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements());
		PagedModel<WsPslDto> result = new PagedModel<>(content, pageMetadata, linkTo(methodOn(WsPslController.class).selectWsPersonalPage(ownerIdx, search, pageable)).withSelfRel());
		commonUtil.setPageLinksAdvice(result, page);
		
		return result;
	}

	@Override
	public WsPslDto selectWsPsl(Long ownerIdx, Long id) {
		Optional<WsPsl> data = wsPslRepository.findByIdAndOwnerIdx(id, ownerIdx);
		
		if(!data.isPresent()) {
			return null;
		}
		
		WsPslDto result = new WsPslDto(data.get());
		result.add(linkTo(methodOn(WsPslController.class).selectWsPersonal(result.getOwnerIdx(), result.getId())).withSelfRel());
		
		return result;
	}

	@Override
	public WsPslDto insertWsPsl(WsPslDto dto) {
		WsPsl wsPersonal = wsPslRepository.save(dto.toEntity());
		
		// 사용자가 명언을 추가할 경우, 해당 명언이 사용자의 오늘의 명언으로 대체 됨
		todaysWsRepository.save(wsPersonal.toTodaysWs());
				
		WsPslDto result = new WsPslDto(wsPersonal);
		result.add(linkTo(methodOn(WsPslController.class).selectWsPersonal(result.getOwnerIdx(), result.getId())).withSelfRel());
		
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
		result.add(linkTo(methodOn(WsPslController.class).selectWsPersonal(result.getOwnerIdx(), result.getId())).withSelfRel());
		
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
