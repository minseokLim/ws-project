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
		
	@Override
	public PagedModel<WsDto> selectWsList(String search, Pageable pageable) {
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
					page = wsRepository.findByContentLike(CommonUtil.getLikeStr(value), pageable);
					break;
				case "author":
					page = wsRepository.findByAuthorLike(CommonUtil.getLikeStr(value), pageable);
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
		
		PagedModel<WsDto> result = setPageLinksAdvice(search, pageable, page);
		
		return result;
	}

	/**
	 * HATEOAS Link 정보 추가
	 * @param search
	 * @param pageable
	 * @param page
	 * @return
	 */
	private PagedModel<WsDto> setPageLinksAdvice(String search, Pageable pageable, Page<Ws> page) {
		List<WsDto> content = page.stream().map(elem -> {
			WsDto dto = new WsDto(elem);
			CommonUtil.setLinkAdvice(dto, linkTo(WsController.class).slash(dto.getId()).withSelfRel());
			return dto;
		}).collect(Collectors.toList());
		
		PageMetadata pageMetadata = new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements());
		PagedModel<WsDto> result = new PagedModel<>(content, pageMetadata);
		CommonUtil.setLinkAdvice(result, linkTo(methodOn(WsController.class).selectWsList(search, pageable)).withSelfRel());
		CommonUtil.setPageLinksAdvice(result, page);
		return result;
	}
	
	@Override
	public WsDto selectWs(Long id) {
		Optional<Ws> data = wsRepository.findById(id);
		
		if(!data.isPresent()) {
			return null;
		}
		
		WsDto result = new WsDto(data.get());
		
		// HATEOAS Link 정보 추가
		CommonUtil.setLinkAdvice(result, linkTo(WsController.class).slash(id).withSelfRel());
		
		return result;
	}

	@Override
	public WsDto insertWs(WsDto dto) {
		Ws ws = wsRepository.save(dto.toEntity());
		WsDto result = new WsDto(ws);
		
		// HATEOAS Link 정보 추가
		CommonUtil.setLinkAdvice(result, linkTo(WsController.class).slash(result.getId()).withSelfRel());
		
		return result;
	}

	@Override
	public WsDto updateWs(Long id, WsDto dto) {
		Optional<Ws> data = wsRepository.findById(id);
		
		if(!data.isPresent()) {
			return null;
		}
		
		// id를 통해 먼저 데이터를 DB에서 조회한 후, 클라이언트로부터 넘어온 정보를 조회한 객체에 반영한다.
		// TODO 이렇게 하지 않을 경우, createDate가 null이 된다. 비효율적으로 보이며 향후 수정이 필요하다.
		Ws ws = data.get();
		ws.update(dto.toEntity());
		
		ws = wsRepository.save(ws);
		
		WsDto result = new WsDto(ws);
		
		// HATEOAS Link 정보 추가
		CommonUtil.setLinkAdvice(result, linkTo(WsController.class).slash(id).withSelfRel());
		
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
}
