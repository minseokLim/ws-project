package com.wsproject.wsservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wsproject.wsservice.domain.Ws;
import com.wsproject.wsservice.domain.enums.WsType;

public interface WsRepository extends JpaRepository<Ws, Long> {
	Page<Ws> findByContentLike(String content, Pageable pageable);
	Page<Ws> findByAuthorLike(String author, Pageable pageable);
	Page<Ws> findByType(WsType type, Pageable pageable);
}