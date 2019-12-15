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

import com.wsproject.wsservice.controller.WS_Controller;
import com.wsproject.wsservice.domain.WS;
import com.wsproject.wsservice.dto.WS_DTO;
import com.wsproject.wsservice.repository.WS_Repository;
import com.wsproject.wsservice.service.WS_Service;
import com.wsproject.wsservice.util.CommonUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WS_ServiceImpl implements WS_Service {

	private WS_Repository repository;
	
	private CommonUtil commonUtil;
	
	@Override
	public PagedModel<WS_DTO> selectWSes(String userEmail, Pageable pageable) {
		Page<WS> page;
		
		if(userEmail != null) {
			page = repository.findByOwnerEmailOrByAdmin(userEmail, true, pageable);
		} else {
			page = repository.findByByAdmin(true, pageable);
		}
		
		List<WS_DTO> content = page.stream().map(elem -> {
			WS_DTO dto = new WS_DTO(elem);
			dto.add(linkTo(WS_Controller.class).slash(dto.getId()).withSelfRel());
			return dto;
		}).collect(Collectors.toList());
		
		PageMetadata pageMetadata = new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements());
		PagedModel<WS_DTO> model = new PagedModel<>(content, pageMetadata, linkTo(methodOn(WS_Controller.class).selectWSes(userEmail, pageable)).withSelfRel());
		commonUtil.setPageLinksAdvice(model, page);
		
		return model;
	}

	@Override
	public WS_DTO selectWSById(Long id) {
		Optional<WS> data = repository.findById(id);
		
		if(!data.isPresent()) {
			return null;
		}
		
		WS_DTO result = new WS_DTO(data.get());
		result.add(linkTo(WS_Controller.class).slash(id).withSelfRel());
		
		return result;
	}

	@Override
	public WS_DTO insertWS(WS_DTO dto) {
		WS ws = repository.save(dto.toEntity());
		WS_DTO result = new WS_DTO(ws);
		result.add(linkTo(WS_Controller.class).slash(result.getId()).withSelfRel());
		
		return result;
	}

	@Override
	public WS_DTO updateWSById(Long id, WS_DTO dto) {
		// 사실 효율성 관점에서는 멍청한 방법이다.
		// createdDate를 가져와야한다. 안그러면 업데이트 하면서 createdDate 값이 null이 되버린다-_-
		// TODO JPA save가 문제인건가...
		Optional<WS> data = repository.findById(id);
		
		if(!data.isPresent()) {
			return null;
		}
		
		WS ws = data.get();
		ws.update(dto.toEntity());
		
		ws = repository.save(ws);
		
		WS_DTO result = new WS_DTO(ws);
		result.add(linkTo(WS_Controller.class).slash(id).withSelfRel());
		
		return result;
	}

	@Override
	public boolean deleteWSById(Long id) {
		Optional<WS> data = repository.findById(id);
		
		if(!data.isPresent()) {
			return false;
		}
		
		repository.deleteById(id);
		return true;
	}
}
