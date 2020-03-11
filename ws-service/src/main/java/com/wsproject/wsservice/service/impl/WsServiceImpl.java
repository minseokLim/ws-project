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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class WsServiceImpl implements WsService {
		
	private WsRepository wsRepository;
		
	private CommonUtil commonUtil;
	
	@Override
	public PagedModel<WsDto> selectWses(String search, Pageable pageable) {
		Page<Ws> page;
		
		if(search == null) {
			page = wsRepository.findAll(pageable);
		} else {
			try {
				// search 파라미터가 'content=내용'과 같은 형식으로 넘어온다.
				search = URLDecoder.decode(search, "UTF-8");
				
				StringTokenizer st = new StringTokenizer(search, "=");
				String key = st.nextToken();
				String value = st.nextToken();
				
				switch (key) {
				case "content":
					page = wsRepository.findByContentLike("%" + value + "%", pageable);
					break;
				case "author":
					page = wsRepository.findByAuthorLike("%" + value + "%", pageable);
					break;
				case "type":
					page = wsRepository.findByType(WsType.valueOf(value), pageable);
					break;
				default:
					return null;
				}
			} catch (UnsupportedEncodingException | NoSuchElementException e) {
				log.error("{} occured, due to invalid parameters.", e.toString());
				return null;
			}	
		}
		
		List<WsDto> content = page.stream().map(elem -> {
			WsDto dto = new WsDto(elem);
			dto.add(linkTo(WsController.class).slash(dto.getId()).withSelfRel());
			return dto;
		}).collect(Collectors.toList());
		
		PageMetadata pageMetadata = new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements());
		PagedModel<WsDto> result = new PagedModel<>(content, pageMetadata, linkTo(methodOn(WsController.class).selectWsPage(search, pageable)).withSelfRel());
		commonUtil.setPageLinksAdvice(result, page);
		
		return result;
	}

	@Override
	public WsDto selectWs(Long id) {
		Optional<Ws> data = wsRepository.findById(id);
		
		if(!data.isPresent()) {
			return null;
		}
		
		WsDto result = new WsDto(data.get());
		result.add(linkTo(WsController.class).slash(id).withSelfRel());
		
		return result;
	}

	@Override
	public WsDto insertWs(WsDto dto) {
		Ws ws = wsRepository.save(dto.toEntity());
		WsDto result = new WsDto(ws);
		result.add(linkTo(WsController.class).slash(result.getId()).withSelfRel());
		
		return result;
	}

	@Override
	public WsDto updateWs(Long id, WsDto dto) {
		// TODO 효율성 관점에서 봤을 때는 좋지 않아보임. 
		// 현재 이 과정 없이 update를 진행하면, createdDate가 null이 되어버림
		Optional<Ws> data = wsRepository.findById(id);
		
		if(!data.isPresent()) {
			return null;
		}
		
		Ws ws = data.get();
		ws.update(dto.toEntity());
		
		ws = wsRepository.save(ws);
		
		WsDto result = new WsDto(ws);
		result.add(linkTo(WsController.class).slash(id).withSelfRel());
		
		return result;
	}

	@Override
	public boolean deleteWs(Long id) {
		Optional<Ws> data = wsRepository.findById(id);
		
		if(!data.isPresent()) {
			return false;
		}
		
		wsRepository.deleteById(id);
		return true;
	}

	@Override
	public long countWs() {
		return wsRepository.count();
	}
}
