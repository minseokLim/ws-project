package com.wsproject.wsservice.service;

public interface LikeService {
	boolean insertLike(Long userIdx, Long wsId, boolean privateFlag);
	boolean deleteLike(Long userIdx, Long wsId, boolean privateFlag);
}
