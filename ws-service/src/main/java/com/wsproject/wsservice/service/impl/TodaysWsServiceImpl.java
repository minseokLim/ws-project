package com.wsproject.wsservice.service.impl;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.wsproject.wsservice.controller.TodaysWsController;
import com.wsproject.wsservice.domain.TodaysWs;
import com.wsproject.wsservice.domain.Ws;
import com.wsproject.wsservice.domain.WsPsl;
import com.wsproject.wsservice.dto.TodaysWsDto;
import com.wsproject.wsservice.repository.TodaysWsRepository;
import com.wsproject.wsservice.repository.WsPslRepository;
import com.wsproject.wsservice.repository.WsRepository;
import com.wsproject.wsservice.service.TodaysWsService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TodaysWsServiceImpl implements TodaysWsService {

	private TodaysWsRepository todaysWsRepository;
	
	private WsRepository wsRepository;
	
	private WsPslRepository wsPslRepository;
	
	@Override
	public TodaysWsDto selectTodaysWs(Long ownerIdx) {
		Optional<TodaysWs> data = todaysWsRepository.findById(ownerIdx);
		TodaysWsDto result;
		
		LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
		
		// 처음 로그인하는 사용자이거나 배치가 돌지 않았을 때(이 로직이 있으면 사실 배치는 필요 없구나...)
		if(!data.isPresent() || data.get().getModifiedDate().isBefore(today)) {
			result = insertTodaysWs(ownerIdx);
		} else {
			result = new TodaysWsDto(data.get());
		}
		
		if(result != null) {
			result.add(linkTo(methodOn(TodaysWsController.class).selectTodaysWs(ownerIdx)).withSelfRel());
		}

		return result;
	}
	
	@Override
	public TodaysWsDto insertTodaysWs(Long ownerIdx) {
		
		long wsCount = wsRepository.count();
		long wsPslCount = wsPslRepository.countByOwnerIdx(ownerIdx);
		long totalWsCount = wsCount + wsPslCount;
		
		TodaysWs todaysWs = null;
			
		int randomNo = (int) (Math.random() * totalWsCount);
		
		if(randomNo < wsCount) {
			Page<Ws> page = wsRepository.findAll(PageRequest.of((int) randomNo, 1));
			
			if(page.getSize() != 0) {
				Ws ws = page.getContent().get(0);
				todaysWs = TodaysWs.builder().userIdx(ownerIdx).content(ws.getContent()).author(ws.getAuthor()).type(ws.getType()).build();
			} 
		} else {
			Page<WsPsl> page  = wsPslRepository.findByOwnerIdx(ownerIdx, PageRequest.of((int) (randomNo - wsCount), 1));
			
			if(page.getSize() != 0) {
				WsPsl wsPsl = page.getContent().get(0);
				todaysWs = TodaysWs.builder().userIdx(ownerIdx).content(wsPsl.getContent()).author(wsPsl.getAuthor()).type(wsPsl.getType()).build();
			} 
		}
		
		todaysWs = todaysWsRepository.save(todaysWs);
		
		TodaysWsDto result = new TodaysWsDto(todaysWs);
		
		result.add(linkTo(methodOn(TodaysWsController.class).selectTodaysWs(result.getUserIdx())).withSelfRel());
		
		return result;
	}
}
