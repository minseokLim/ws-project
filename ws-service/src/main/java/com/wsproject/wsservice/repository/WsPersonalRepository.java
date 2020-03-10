package com.wsproject.wsservice.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wsproject.wsservice.domain.WsPersonal;

public interface WsPersonalRepository extends JpaRepository<WsPersonal, Long> {
	Page<WsPersonal> findByOwnerEmail(String ownerEmail, Pageable pageable);
	Optional<WsPersonal> findByIdAndOwnerEmail(Long id, String ownerEmail);

	@Query(value = "SELECT MIN(id) FROM TBL_WS_PSL", nativeQuery = true)
	Long findMinId();
	
	@Query(value = "SELECT MAX(id) FROM TBL_WS_PSL", nativeQuery = true)
	Long findMaxId();
}
