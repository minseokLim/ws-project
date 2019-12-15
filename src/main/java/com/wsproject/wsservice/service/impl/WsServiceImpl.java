package com.wsproject.wsservice.service.impl;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.stereotype.Service;

import com.wsproject.wsservice.controller.WsController;
import com.wsproject.wsservice.domain.Ws;
import com.wsproject.wsservice.domain.enums.WsType;
import com.wsproject.wsservice.dto.WsDto;
import com.wsproject.wsservice.repository.WsRepository;
import com.wsproject.wsservice.service.WsService;
import com.wsproject.wsservice.util.CommonUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WsServiceImpl implements WsService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WsServiceImpl.class);
	
	private WsRepository repository;
	
	private CommonUtil commonUtil;
	
	@Override
	public PagedModel<WsDto> selectWses(String search, Pageable pageable) {
		Page<Ws> page;
		
		if(search == null) {
			page = repository.findAll(pageable);
		} else {
			try {
				// search 파라미터가 'content=내용'과 같은 형식으로 넘어온다.
				search = URLDecoder.decode(search, "UTF-8");
				
				StringTokenizer st = new StringTokenizer(search, "=");
				String key = st.nextToken();
				String value = st.nextToken();
				
				switch (key) {
				case "content":
					page = repository.findByContentLike("%" + value + "%", pageable);
					break;
				case "author":
					page = repository.findByAuthorLike("%" + value + "%", pageable);
					break;
				case "type":
					page = repository.findByType(WsType.valueOf(value), pageable);
					break;
				default:
					return null;
				}
			} catch (UnsupportedEncodingException | NoSuchElementException e) {
				LOGGER.error("{} occured, due to invalid parameters.", e.toString());
				return null;
			}	
		}
		
		List<WsDto> content = page.stream().map(elem -> {
			WsDto dto = new WsDto(elem);
			dto.add(linkTo(WsController.class).slash(dto.getId()).withSelfRel());
			return dto;
		}).collect(Collectors.toList());
		
		PageMetadata pageMetadata = new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements());
		PagedModel<WsDto> result = new PagedModel<>(content, pageMetadata, linkTo(methodOn(WsController.class).selectWses(search, pageable)).withSelfRel());
		commonUtil.setPageLinksAdvice(result, page);
		
		return result;
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
		// TODO 효율성 관점에서 봤을 때는 좋지 않아보임. 
		// 현재 이 과정 없이 update를 진행하면, createdDate가 null이 되어버림
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
