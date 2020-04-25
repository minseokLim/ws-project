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
import com.wsproject.wsservice.repository.WsPslRepository;
import com.wsproject.wsservice.service.WsPslService;
import com.wsproject.wsservice.util.CommonUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WsPslServiceImpl implements WsPslService {
		
	private WsPslRepository wsPslRepository;
	
	@Override
	public PagedModel<WsPslDto> selectWsPslList(Long ownerIdx, String search, Pageable pageable) {
		// 일단 search에 대한 부분은 미구현
		Page<WsPsl> page = wsPslRepository.findByOwnerIdx(ownerIdx, pageable);
		
		PagedModel<WsPslDto> result = setPageLinksAdvice(ownerIdx, search, pageable, page);
		
		return result;
	}

	/**
	 * HATEOAS Link 정보 추가
	 * @param ownerIdx
	 * @param search
	 * @param pageable
	 * @param page
	 * @return
	 */
	private PagedModel<WsPslDto> setPageLinksAdvice(Long ownerIdx, String search, Pageable pageable, Page<WsPsl> page) {
		List<WsPslDto> content = page.stream().map(elem -> {
			WsPslDto dto = new WsPslDto(elem);
			CommonUtil.setLinkAdvice(dto, linkTo(methodOn(WsPslController.class).selectWsPsl(ownerIdx, dto.getId())).withSelfRel());
			return dto;
		}).collect(Collectors.toList());
		
		PageMetadata pageMetadata = new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements());
		PagedModel<WsPslDto> result = new PagedModel<>(content, pageMetadata);
		CommonUtil.setLinkAdvice(result, linkTo(methodOn(WsPslController.class).selectWsPslList(ownerIdx, search, pageable)).withSelfRel());
		CommonUtil.setPageLinksAdvice(result, page);
		return result;
	}

	@Override
	public WsPslDto selectWsPsl(Long ownerIdx, Long id) {
		Optional<WsPsl> data = wsPslRepository.findByIdAndOwnerIdx(id, ownerIdx);
		
		if(!data.isPresent()) {
			return null;
		}
		
		WsPslDto result = new WsPslDto(data.get());
		
		// HATEOAS Link 정보 추가
		CommonUtil.setLinkAdvice(result, linkTo(methodOn(WsPslController.class).selectWsPsl(result.getOwnerIdx(), result.getId())).withSelfRel());
		
		return result;
	}

	@Override
	public WsPslDto insertWsPsl(WsPslDto dto) {
		WsPsl wsPsl = wsPslRepository.save(dto.toEntity());
				
		WsPslDto result = new WsPslDto(wsPsl);
		
		// HATEOAS Link 정보 추가
		CommonUtil.setLinkAdvice(result, linkTo(methodOn(WsPslController.class).selectWsPsl(result.getOwnerIdx(), result.getId())).withSelfRel());
		
		return result;
	}

	@Override
	public WsPslDto updateWsPsl(Long ownerIdx, Long id, WsPslDto dto) {
		Optional<WsPsl> data = wsPslRepository.findByIdAndOwnerIdx(id, ownerIdx);
		
		if(!data.isPresent()) {
			return null;
		}
		
		// id를 통해 먼저 데이터를 DB에서 조회한 후, 클라이언트로부터 넘어온 정보를 조회한 객체에 반영한다.
		// TODO 이렇게 하지 않을 경우, createDate가 null이 된다. 비효율적으로 보이며 향후 수정이 필요하다.
		WsPsl wsPsl = data.get();
		wsPsl.update(dto.toEntity());
		
		wsPsl = wsPslRepository.save(wsPsl);
		
		WsPslDto result = new WsPslDto(wsPsl);
		
		// HATEOAS Link 정보 추가
		CommonUtil.setLinkAdvice(result, linkTo(methodOn(WsPslController.class).selectWsPsl(result.getOwnerIdx(), result.getId())).withSelfRel());
		
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
