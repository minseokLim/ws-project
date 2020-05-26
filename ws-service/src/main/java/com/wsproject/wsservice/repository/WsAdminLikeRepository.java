package com.wsproject.wsservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wsproject.wsservice.domain.WsAdmin;
import com.wsproject.wsservice.domain.WsAdminLike;

public interface WsAdminLikeRepository extends JpaRepository<WsAdminLike, Long> {
	Optional<WsAdminLike> findByWsAndUserIdx(WsAdmin ws, Long userIdx);
}
