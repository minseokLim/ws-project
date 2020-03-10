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

import com.wsproject.wsservice.controller.WsPersonalController;
import com.wsproject.wsservice.domain.WsPersonal;
import com.wsproject.wsservice.dto.WsPersonalDto;
import com.wsproject.wsservice.repository.WsPersonalRepository;
import com.wsproject.wsservice.service.WsPersonalService;
import com.wsproject.wsservice.util.CommonUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WsPersonalServiceImpl implements WsPersonalService {
		
	private WsPersonalRepository repository;
	
	private CommonUtil commonUtil;
	
	@Override
	public PagedModel<WsPersonalDto> selectWsPersonals(String userEmail, String search, Pageable pageable) {
		Page<WsPersonal> page;
		
		// 일단 search에 대한 부분은 미구현
		page = repository.findByOwnerEmail(userEmail, pageable);

		List<WsPersonalDto> content = page.stream().map(elem -> {
			WsPersonalDto dto = new WsPersonalDto(elem);
			dto.add(linkTo(methodOn(WsPersonalController.class).selectWsPersonal(userEmail, dto.getId())).withSelfRel());
			return dto;
		}).collect(Collectors.toList());
		
		PageMetadata pageMetadata = new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements());
		PagedModel<WsPersonalDto> result = new PagedModel<>(content, pageMetadata, linkTo(methodOn(WsPersonalController.class).selectWsPersonalPage(userEmail, search, pageable)).withSelfRel());
		commonUtil.setPageLinksAdvice(result, page);
		
		return result;
	}

	@Override
	public WsPersonalDto selectWsPersonal(String userEmail,Long id) {
		Optional<WsPersonal> data = repository.findByIdAndOwnerEmail(id, userEmail);
		
		if(!data.isPresent()) {
			return null;
		}
		
		WsPersonalDto result = new WsPersonalDto(data.get());
		result.add(linkTo(methodOn(WsPersonalController.class).selectWsPersonal(result.getOwnerEmail(), result.getId())).withSelfRel());
		
		return result;
	}

	@Override
	public WsPersonalDto insertWsPersonal(WsPersonalDto dto) {
		WsPersonal wsPersonal = repository.save(dto.toEntity());
		WsPersonalDto result = new WsPersonalDto(wsPersonal);
		result.add(linkTo(methodOn(WsPersonalController.class).selectWsPersonal(result.getOwnerEmail(), result.getId())).withSelfRel());
		
		return result;
	}

	@Override
	public WsPersonalDto updateWsPersonal(String userEmail, Long id, WsPersonalDto dto) {
		Optional<WsPersonal> data = repository.findByIdAndOwnerEmail(id, userEmail);
		
		if(!data.isPresent()) {
			return null;
		}
		
		WsPersonal wsPersonal = data.get();
		wsPersonal.update(dto.toEntity());
		
		wsPersonal = repository.save(wsPersonal);
		
		WsPersonalDto result = new WsPersonalDto(wsPersonal);
		result.add(linkTo(methodOn(WsPersonalController.class).selectWsPersonal(result.getOwnerEmail(), result.getId())).withSelfRel());
		
		return result;
	}

	@Override
	public boolean deleteWsPersonal(String userEmail, Long id) {
		Optional<WsPersonal> data = repository.findByIdAndOwnerEmail(id, userEmail);
		
		if(!data.isPresent()) {
			return false;
		}
		
		repository.deleteById(id);
		return true;
	}

}
