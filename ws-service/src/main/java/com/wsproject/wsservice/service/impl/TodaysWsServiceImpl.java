package com.wsproject.wsservice.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wsproject.wsservice.domain.TodaysWs;
import com.wsproject.wsservice.domain.WsAdmin;
import com.wsproject.wsservice.domain.WsPrivate;
import com.wsproject.wsservice.dto.TodaysWsResponseDto;
import com.wsproject.wsservice.repository.TodaysWsRepository;
import com.wsproject.wsservice.repository.TodaysWsRepositorySupport;
import com.wsproject.wsservice.repository.WsAdminRepository;
import com.wsproject.wsservice.repository.WsPrivateRepository;
import com.wsproject.wsservice.service.TodaysWsService;

import brave.Span;
import brave.Tracer;
import brave.Tracer.SpanInScope;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class TodaysWsServiceImpl implements TodaysWsService {

	private TodaysWsRepository todaysWsRepository;
	
	private TodaysWsRepositorySupport todaysWsRepositorySupport;
	
	private WsAdminRepository wsAdminRepository;
	
	private WsPrivateRepository wsPrivateRepository;
	
	private Tracer tracer;
	
	@Override
	public TodaysWsResponseDto selectTodaysWs(Long userIdx) {
		
		Optional<TodaysWs> data = null;
		
		// TODO 불필요한 로직. 슬루스 및 집킨에서 사용자 정의 스팬을 테스트해보기 위해 추가
		Span newSpan = tracer.nextSpan().name("findTodaysWsByOwnerIdx");
		
		try(SpanInScope spanInScope = tracer.withSpanInScope(newSpan.start())) {
//			data = todaysWsRepositorySupport.findWithLikeByUserIdx(userIdx);
			data = todaysWsRepositorySupport.findByUserIdx(userIdx); // 1차, 2차 캐시 사용을 위해선 dto가 아닌 entity로 조회해야함
		} finally {
			newSpan.tag("peer.service", "mariaDB");
			newSpan.annotate("test");
			newSpan.finish();
		}
		
		TodaysWsResponseDto result;
		
		LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
		
		// 데이터가 없거나 조회된 데이터가 어제 것일 때
		// TODO 현재 배치(오늘의 명언 업데이트 배치)는 필요없음. 향후 다른 배치를 추가할 예정
		if(!data.isPresent() || data.get().getModifiedDate().isBefore(today)) {
			result = refreshTodaysWs(userIdx);
		} else {
			result = new TodaysWsResponseDto(data.get());
		}
		
		return result;
	}

	@Override
	public TodaysWsResponseDto refreshTodaysWs(Long userIdx) {
		Optional<TodaysWs> data = todaysWsRepositorySupport.findByUserIdx(userIdx);
		TodaysWs todaysWs = data.orElse(new TodaysWs());
		todaysWs.update(getRandomTodaysWs(userIdx));
		
		TodaysWsResponseDto result = new TodaysWsResponseDto(todaysWsRepository.save(todaysWs));
		
		return result;
	}
	
	/**
	 * 관리자가 추가한 명언 + 사용자가 추가한 명언 중 랜덤으로 오늘의 명언이 선택되어 반환된다.
	 * @param userIdx
	 * @return
	 */
	private TodaysWs getRandomTodaysWs(Long userIdx) {
		TodaysWs todaysWs = null;
		
		long wsAdminCount = wsAdminRepository.count();
		long wsPrivateCount = wsPrivateRepository.countByOwnerIdx(userIdx);
		long totalWsCount = wsAdminCount + wsPrivateCount;
		
		int randomNo = (int) (Math.random() * totalWsCount);
		
		if(randomNo < wsAdminCount) {
			Page<WsAdmin> page = wsAdminRepository.findAll(PageRequest.of((int) randomNo, 1));
			
			WsAdmin wsAdmin = page.getContent().get(0);
			todaysWs = TodaysWs.builder().userIdx(userIdx).wsAdmin(wsAdmin).build();
		} else {
			Page<WsPrivate> page  = wsPrivateRepository.findByOwnerIdx(userIdx, PageRequest.of((int) (randomNo - wsAdminCount), 1));
			
			WsPrivate wsPrivate = page.getContent().get(0);
			todaysWs = TodaysWs.builder().userIdx(userIdx).wsPrivate(wsPrivate).build();
		}
		
		return todaysWs;
	}
}
