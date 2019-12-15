package com.wsproject.wsservice.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wsproject.wsservice.domain.WsPersonal;

public interface WsPersonalRepository extends JpaRepository<WsPersonal, Long> {
	Page<WsPersonal> findByOwnerEmail(String ownerEmail, Pageable pageable);
	Optional<WsPersonal> findByIdAndOwnerEmail(Long id, String ownerEmail);
}
