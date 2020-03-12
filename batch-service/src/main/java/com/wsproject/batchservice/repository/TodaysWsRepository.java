package com.wsproject.batchservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wsproject.batchservice.domain.TodaysWs;

public interface TodaysWsRepository extends JpaRepository<TodaysWs, Long> {

}
