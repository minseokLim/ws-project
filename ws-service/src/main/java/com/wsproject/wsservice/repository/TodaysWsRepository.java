package com.wsproject.wsservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wsproject.wsservice.domain.TodaysWs;

public interface TodaysWsRepository extends JpaRepository<TodaysWs, Long> {
	Optional<TodaysWs> findByUserIdx(Long userIdx);
}
