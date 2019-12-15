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

import com.wsproject.wsservice.controller.WsController;
import com.wsproject.wsservice.domain.Ws;
import com.wsproject.wsservice.dto.WsDto;
import com.wsproject.wsservice.repository.WsRepository;
import com.wsproject.wsservice.service.WsService;
import com.wsproject.wsservice.util.CommonUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WsServiceImpl implements WsService {

	private WsRepository repository;
	
	private CommonUtil commonUtil;
	
	@Override
	public PagedModel<WsDto> selectWses(String userEmail, Pageable pageable) {
		Page<Ws> page;
		
		if(userEmail != null) {
			page = repository.findByOwnerEmailOrByAdmin(userEmail, true, pageable);
		} else {
			page = repository.findByByAdmin(true, pageable);
		}
		
		List<WsDto> content = page.stream().map(elem -> {
			WsDto dto = new WsDto(elem);
			dto.add(linkTo(WsController.class).slash(dto.getId()).withSelfRel());
			return dto;
		}).collect(Collectors.toList());
		
		PageMetadata pageMetadata = new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements());
		PagedModel<WsDto> model = new PagedModel<>(content, pageMetadata, linkTo(methodOn(WsController.class).selectWses(userEmail, pageable)).withSelfRel());
		commonUtil.setPageLinksAdvice(model, page);
		
		return model;
	}

	@Override
	public WsDto selectWsById(Long id) {
		Optional<Ws> data = repository.findById(id);
		
		if(!data.isPresent()) {
			return null;
		}
		
		WsDto result = new WsDto(data.get());
		result.add(linkTo(WsController.class).slash(id).withSelfRel());
		
		return result;
	}

	@Override
	public WsDto insertWs(WsDto dto) {
		Ws ws = repository.save(dto.toEntity());
		WsDto result = new WsDto(ws);
		result.add(linkTo(WsController.class).slash(result.getId()).withSelfRel());
		
		return result;
	}

	@Override
	public WsDto updateWsById(Long id, WsDto dto) {
		// 사실 효율성 관점에서는 멍청한 방법이다.
		// createdDate를 가져와야한다. 안그러면 업데이트 하면서 createdDate 값이 null이 되버린다-_-
		// TODO JPA save가 문제인건가...
		Optional<Ws> data = repository.findById(id);
		
		if(!data.isPresent()) {
			return null;
		}
		
		Ws ws = data.get();
		ws.update(dto.toEntity());
		
		ws = repository.save(ws);
		
		WsDto result = new WsDto(ws);
		result.add(linkTo(WsController.class).slash(id).withSelfRel());
		
		return result;
	}

	@Override
	public boolean deleteWsById(Long id) {
		Optional<Ws> data = repository.findById(id);
		
		if(!data.isPresent()) {
			return false;
		}
		
		repository.deleteById(id);
		return true;
	}
}
