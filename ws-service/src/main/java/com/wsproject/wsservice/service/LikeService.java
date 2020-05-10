package com.wsproject.wsservice.service;

import com.wsproject.wsservice.domain.Like;
import com.wsproject.wsservice.domain.LikeId;
import com.wsproject.wsservice.dto.LikeDto;

public interface LikeService {
	Like insertLike(LikeDto likeDto);
	void deleteLike(LikeId likeId);
}
