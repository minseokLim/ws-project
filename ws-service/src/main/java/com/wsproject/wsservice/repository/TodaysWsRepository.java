package com.wsproject.wsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wsproject.wsservice.domain.TodaysWs;

public interface TodaysWsRepository extends JpaRepository<TodaysWs, Long> {

}
