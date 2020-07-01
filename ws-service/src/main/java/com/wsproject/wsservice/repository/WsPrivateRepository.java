package com.wsproject.wsservice.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.wsproject.wsservice.domain.WsPrivate;

public interface WsPrivateRepository extends JpaRepository<WsPrivate, Long>, QuerydslPredicateExecutor<WsPrivate> {
	Page<WsPrivate> findByOwnerIdx(Long ownerIdx, Pageable pageable);
//	Page<WsPrivate> findByOwnerIdxAndContentContaining(Long ownerIdx, String value, Pageable pageable);
//	Page<WsPrivate> findByOwnerIdxAndAuthorContaining(Long ownerIdx, String value, Pageable pageable);
//	Page<WsPrivate> findByOwnerIdxAndType(Long ownerIdx, WsType type, Pageable pageable);
	
	Optional<WsPrivate> findByIdAndOwnerIdx(Long id, Long ownerIdx);
	long countByOwnerIdx(Long ownerIdx);
}
