package com.wsproject.wsservice.service.impl;

import org.springframework.stereotype.Service;

import com.wsproject.wsservice.domain.Like;
import com.wsproject.wsservice.domain.LikeId;
import com.wsproject.wsservice.dto.LikeDto;
import com.wsproject.wsservice.repository.LikeRepository;
import com.wsproject.wsservice.service.LikeService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {

	
	private LikeRepository likeRepository;
	
	@Override
	public Like insertLike(LikeDto likeDto) {
		return likeRepository.save(likeDto.toEntity());
	}

	@Override
	public void deleteLike(LikeId likeId) {
		likeRepository.deleteById(likeId);
	}
}
