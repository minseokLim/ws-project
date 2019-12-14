package com.wsproject.wsservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wsproject.wsservice.domain.WS;

public interface WS_Repository extends JpaRepository<WS, Long> {
	Page<WS> findByOwnerEmailOrByAdmin(String ownerEmail, boolean byAdmin, Pageable pageable);
	Page<WS> findByByAdmin(boolean byAdmin, Pageable pageable);
}
