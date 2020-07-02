package com.wsproject.wsservice.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wsproject.wsservice.domain.WsAdmin;
import com.wsproject.wsservice.domain.WsAdminLike;
import com.wsproject.wsservice.domain.WsPrivate;
import com.wsproject.wsservice.repository.WsAdminLikeRepository;
import com.wsproject.wsservice.repository.WsAdminRepository;
import com.wsproject.wsservice.repository.WsPrivateRepository;
import com.wsproject.wsservice.service.LikeService;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {

	private WsAdminLikeRepository wsAdminLikeRepository;
	
	private WsAdminRepository wsAdminRepository;
	
	private WsPrivateRepository wsPrivateRepository;

	@Override
	public boolean insertLike(Long userIdx, Long wsId, boolean privateFlag) {
				
		if(privateFlag) {
			Optional<WsPrivate> ws = wsPrivateRepository.findById(wsId);
			
			if(!ws.isPresent()) {
				return false;
			}
			
			WsPrivate wsPrivate = ws.get();
			wsPrivate.setLiked(true);
			
			// TODO 변경감지?
			wsPrivateRepository.save(wsPrivate);
			
		} else {
			Optional<WsAdmin> ws = wsAdminRepository.findById(wsId);
			
			if(!ws.isPresent()) {
				return false;
			}
			
			WsAdminLike like = WsAdminLike.builder().ws(ws.get()).userIdx(userIdx).build();
			
			wsAdminLikeRepository.save(like);
		}
		
		return true;
	}

	@Override
	public boolean deleteLike(Long userIdx, Long wsId, boolean privateFlag) {
		
		if(privateFlag) {
			Optional<WsPrivate> ws = wsPrivateRepository.findById(wsId);
			
			if(!ws.isPresent()) {
				return false;
			}
			
			WsPrivate wsPrivate = ws.get();
			wsPrivate.setLiked(false);
			
			// TODO 변경감지?
			wsPrivateRepository.save(wsPrivate);
			
		} else {
			Optional<WsAdmin> ws = wsAdminRepository.findById(wsId);
			
			if(!ws.isPresent()) {
				return false;
			}
			
			Optional<WsAdminLike> like = wsAdminLikeRepository.findByWsAndUserIdx(ws.get(), userIdx);
			
			if(!like.isPresent()) {
				return false;
			}
			
			wsAdminLikeRepository.deleteById(like.get().getId());
		}
		
		return true;
	}
}
