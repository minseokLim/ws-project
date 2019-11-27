package com.wsProject.wsService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wsProject.wsService.domain.WS;

@RepositoryRestResource
public interface WS_Repository extends JpaRepository<WS, Long> {

}
