package com.wsproject.wsservice.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wsproject.wsservice.domain.WsPrivate;
import com.wsproject.wsservice.domain.enums.WsType;

public interface WsPrivateRepository extends JpaRepository<WsPrivate, Long> {
	Page<WsPrivate> findByOwnerIdx(Long ownerIdx, Pageable pageable);
	Page<WsPrivate> findByOwnerIdxAndContentLike(Long ownerIdx, String content, Pageable pageable);
	Page<WsPrivate> findByOwnerIdxAndAuthorLike(Long ownerIdx, String author, Pageable pageable);
	Page<WsPrivate> findByOwnerIdxAndType(Long ownerIdx, WsType type, Pageable pageable);
	
	Optional<WsPrivate> findByIdAndOwnerIdx(Long id, Long ownerIdx);
	long countByOwnerIdx(Long ownerIdx);
}
