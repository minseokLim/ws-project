package com.wsproject.wsservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wsproject.wsservice.domain.WsAdmin;
import com.wsproject.wsservice.domain.enums.WsType;

public interface WsAdminRepository extends JpaRepository<WsAdmin, Long> {
	Page<WsAdmin> findByType(WsType type, Pageable pageable);
	Page<WsAdmin> findByContentContaining(String value, Pageable pageable);
	Page<WsAdmin> findByAuthorContaining(String value, Pageable pageable);
}
