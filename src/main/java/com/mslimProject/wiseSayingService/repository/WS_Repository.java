package com.mslimProject.wiseSayingService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mslimProject.wiseSayingService.domain.WiseSaying;

@RepositoryRestResource
public interface WS_Repository extends JpaRepository<WiseSaying, Long> {

}
