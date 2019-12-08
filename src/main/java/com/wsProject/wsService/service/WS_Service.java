package com.wsProject.wsService.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.stereotype.Service;

import com.wsProject.wsService.controller.WS_Controller;
import com.wsProject.wsService.domain.WS;
import com.wsProject.wsService.dto.WS_DTO;
import com.wsProject.wsService.repository.WS_Repository;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class WS_Service {
	
	private WS_Repository repository;
	
	public PagedModel<WS_DTO> getWSes(Pageable pageable, String userEmail) {
		Page<WS> wses = repository.findAllWithLike(pageable, userEmail);
		
		List<WS_DTO> content = wses.stream().map(elem -> {
			WS_DTO dto = new WS_DTO(elem);
			Link link = linkTo(WS_Controller.class).slash(elem.getId()).withSelfRel();
			dto.add(link);
			return dto;
		}).collect(Collectors.toList());
		
		PageMetadata pageMetadata = new PageMetadata(pageable.getPageSize(), wses.getNumber(), wses.getTotalElements());
		PagedModel<WS_DTO> model = new PagedModel<>(content, pageMetadata, linkTo(methodOn(WS_Controller.class).getWSes(pageable, userEmail)).withSelfRel());
		
		return model;
	}

	public WS_DTO getWSById(Long id, String userEmail) {
		WS ws = repository.findByIdWithLike(id, userEmail);
		WS_DTO dto = new WS_DTO(ws);
		Link link = linkTo(WS_Controller.class).slash(ws.getId()).withSelfRel();
		dto.add(link);
		
		return dto;
	}
}
