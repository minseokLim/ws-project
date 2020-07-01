package com.wsproject.wsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.wsproject.wsservice.domain.WsAdmin;

public interface WsAdminRepository extends JpaRepository<WsAdmin, Long>, QuerydslPredicateExecutor<WsAdmin> {
//	Page<WsAdmin> findByType(WsType type, Pageable pageable);
//	Page<WsAdmin> findByContentContaining(String value, Pageable pageable);
//	Page<WsAdmin> findByAuthorContaining(String value, Pageable pageable);
}
