package com.wsproject.wsservice.service.impl;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.wsproject.wsservice.controller.TodaysWsController;
import com.wsproject.wsservice.domain.Like;
import com.wsproject.wsservice.domain.LikeId;
import com.wsproject.wsservice.domain.TodaysWs;
import com.wsproject.wsservice.domain.Ws;
import com.wsproject.wsservice.domain.WsPsl;
import com.wsproject.wsservice.dto.TodaysWsDto;
import com.wsproject.wsservice.repository.LikeRepository;
import com.wsproject.wsservice.repository.TodaysWsRepository;
import com.wsproject.wsservice.repository.WsPslRepository;
import com.wsproject.wsservice.repository.WsRepository;
import com.wsproject.wsservice.service.TodaysWsService;
import com.wsproject.wsservice.util.CommonUtil;

import brave.Span;
import brave.Tracer;
import brave.Tracer.SpanInScope;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TodaysWsServiceImpl implements TodaysWsService {

	private TodaysWsRepository todaysWsRepository;
	
	private WsRepository wsRepository;
	
	private WsPslRepository wsPslRepository;
	
	private LikeRepository likeRepository;
	
	private Tracer tracer;
	
	@Override
	public TodaysWsDto selectTodaysWs(Long ownerIdx) {
		
		Optional<TodaysWs> data = null; 
		
		// TODO 불필요한 로직. 슬루스 및 집킨에서 사용자 정의 스팬을 테스트해보기 위해 추가
		Span newSpan = tracer.nextSpan().name("findTodaysWsByOwnerIdx");
		
		try(SpanInScope spanInScope = tracer.withSpanInScope(newSpan.start())) {
			data = todaysWsRepository.findById(ownerIdx);
		} finally {
			newSpan.tag("peer.service", "mariaDB");
			newSpan.annotate("test");
			newSpan.finish();
		}
		
		TodaysWsDto result;
		
		LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
		
		// 처음 로그인하는 사용자이거나 조회된 데이터가 어제 것일 때
		// TODO 현재 배치(오늘의 명언 업데이트 배치)는 필요없음. 향후 다른 배치를 추가할 예정
		if(!data.isPresent() || data.get().getModifiedDate().isBefore(today)) {
			TodaysWs todaysWs = data.orElse(new TodaysWs());
			todaysWs.update(getRandomTodaysWs(ownerIdx));
			
			result = new TodaysWsDto(todaysWsRepository.save(todaysWs));
		} else {
			result = new TodaysWsDto(data.get());
		}
		
		// TODO 원래 JPA의 연관관계 매핑을 통해 처리해야할 것 같으나 JPA 지식이 짧은 관계로 일단 이렇게 처리한 후 나중에 공부해서 바꿀 예정
		result.setLiked(isLiked(result));
		
		// HATEOAS Link 정보 추가
		CommonUtil.setLinkAdvice(result, linkTo(methodOn(TodaysWsController.class).selectTodaysWs(ownerIdx)).withSelfRel());

		return result;
	}

	@Override
	public TodaysWsDto refreshTodaysWs(Long ownerIdx) {
		Optional<TodaysWs> data = todaysWsRepository.findById(ownerIdx);
		TodaysWs todaysWs = data.orElse(new TodaysWs());
		todaysWs.update(getRandomTodaysWs(ownerIdx));
		
		TodaysWsDto result = new TodaysWsDto(todaysWsRepository.save(todaysWs));
		
		// TODO 원래 JPA의 연관관계 매핑을 통해 처리해야할 것 같으나 JPA 지식이 짧은 관계로 일단 이렇게 처리한 후 나중에 공부해서 바꿀 예정
		result.setLiked(isLiked(result));
				
		// HATEOAS Link 정보 추가
		CommonUtil.setLinkAdvice(result, linkTo(methodOn(TodaysWsController.class).selectTodaysWs(ownerIdx)).withSelfRel());
		
		return result;
	}
	
	/**
	 * 관리자가 추가한 명언 + 사용자가 추가한 명언 중 랜덤으로 오늘의 명언이 선택되어 반환된다.
	 * @param ownerIdx
	 * @return
	 */
	private TodaysWs getRandomTodaysWs(Long ownerIdx) {
		TodaysWs todaysWs = null;
		
		long wsCount = wsRepository.count();
		long wsPslCount = wsPslRepository.countByOwnerIdx(ownerIdx);
		long totalWsCount = wsCount + wsPslCount;
		
		int randomNo = (int) (Math.random() * totalWsCount);
		
		if(randomNo < wsCount) {
			Page<Ws> page = wsRepository.findAll(PageRequest.of((int) randomNo, 1));
			
			if(page.getSize() != 0) {
				Ws ws = page.getContent().get(0);
				todaysWs = TodaysWs.builder().userIdx(ownerIdx).content(ws.getContent()).author(ws.getAuthor()).type(ws.getType()).wsId(ws.getId()).psl(false).build();
			} 
		} else {
			Page<WsPsl> page  = wsPslRepository.findByOwnerIdx(ownerIdx, PageRequest.of((int) (randomNo - wsCount), 1));
			
			if(page.getSize() != 0) {
				WsPsl wsPsl = page.getContent().get(0);
				todaysWs = TodaysWs.builder().userIdx(ownerIdx).content(wsPsl.getContent()).author(wsPsl.getAuthor()).type(wsPsl.getType()).wsId(wsPsl.getId()).psl(true).build();
			} 
		}
		
		return todaysWs;
	}
	
	private boolean isLiked(TodaysWsDto dto) {
		boolean ret = false;
		
		LikeId likeId = LikeId.builder().wsId(dto.getWsId()).psl(dto.isPsl()).userIdx(dto.getUserIdx()).build();
		
		Optional<Like> like = likeRepository.findById(likeId);
		
		if(like.isPresent()) {
			ret = true;
		}
		
		return ret;
	}
}
