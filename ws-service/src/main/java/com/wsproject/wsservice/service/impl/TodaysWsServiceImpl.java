package com.wsproject.wsservice.service.impl;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wsproject.wsservice.controller.TodaysWsController;
import com.wsproject.wsservice.domain.TodaysWs;
import com.wsproject.wsservice.dto.TodaysWsDto;
import com.wsproject.wsservice.repository.TodaysWsRepository;
import com.wsproject.wsservice.service.TodaysWsService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TodaysWsServiceImpl implements TodaysWsService {

	private TodaysWsRepository repository;
	
	@Override
	public TodaysWsDto selectTodaysWs(Long ownerIdx) {
		Optional<TodaysWs> data = repository.findById(ownerIdx);
		
		if(!data.isPresent()) {
			return null;
		}
		
		TodaysWsDto result = new TodaysWsDto(data.get());
		result.add(linkTo(methodOn(TodaysWsController.class).selectTodaysWs(ownerIdx)).withSelfRel());

		return result;
	}
	
	@Override
	public TodaysWsDto insertTodaysWs(TodaysWsDto dto) {
		TodaysWs todaysWs = repository.save(dto.toEntity());
		TodaysWsDto result = new TodaysWsDto(todaysWs);
		
		result.add(linkTo(methodOn(TodaysWsController.class).selectTodaysWs(result.getUserIdx())).withSelfRel());
		
		return result;
	}
}
