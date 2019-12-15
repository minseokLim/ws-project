package com.wsproject.wsservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wsproject.wsservice.domain.Ws;

public interface WsRepository extends JpaRepository<Ws, Long> {
	Page<Ws> findByOwnerEmailOrByAdmin(String ownerEmail, boolean byAdmin, Pageable pageable);
	Page<Ws> findByByAdmin(boolean byAdmin, Pageable pageable);
}
