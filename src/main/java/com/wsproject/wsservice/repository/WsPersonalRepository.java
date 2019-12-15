package com.wsproject.wsservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wsproject.wsservice.domain.WsPersonal;

public interface WsPersonalRepository extends JpaRepository<WsPersonal, Long> {
	Page<WsPersonal> findByOwnerEmail(String ownerEmail, Pageable pageable);
}
