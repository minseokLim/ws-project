package com.wsproject.wsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wsproject.wsservice.domain.Like;
import com.wsproject.wsservice.domain.LikeId;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
	
}
