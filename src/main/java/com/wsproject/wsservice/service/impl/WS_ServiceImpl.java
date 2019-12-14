package com.wsproject.wsservice.service.impl;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

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
@Transactional
@AllArgsConstructor
public class WS_ServiceImpl implements WS_Service {

	private WS_Repository repository;
	
	private CommonUtil commonUtil;
	
	@Override
	public PagedModel<WS_DTO> findWSesByUserEmail(String userEmail, Pageable pageable) {
		Page<WS> page = repository.findByOwnerEmailOrByAdmin(userEmail, true, pageable);
		
		List<WS_DTO> content = page.stream().map(elem -> {
			WS_DTO dto = new WS_DTO(elem);
			dto.add(linkTo(WS_Controller.class).slash(elem.getId()).withSelfRel());
			return dto;
		}).collect(Collectors.toList());
		
		PageMetadata pageMetadata = new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements());
		PagedModel<WS_DTO> model = new PagedModel<>(content, pageMetadata, linkTo(methodOn(WS_Controller.class).findWSesByUserEmail(userEmail, pageable)).withSelfRel());
		commonUtil.setPageLinksAdvice(model, page);
		
		return model;
	}
//
//	public WS_DTO getWSById(Long id, String userEmail) {
//		WS ws = repository.findByIdWithLike(id, userEmail);
//		WS_DTO dto = new WS_DTO(ws);
//		Link link = linkTo(WS_Controller.class).slash(ws.getId()).withSelfRel();
//		dto.add(link);
//		
//		return dto;
//	}
}
