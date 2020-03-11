package com.wsproject.wsservice.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wsproject.wsservice.domain.WsPsl;

public interface WsPslRepository extends JpaRepository<WsPsl, Long> {
	Page<WsPsl> findByOwnerIdx(Long ownerIdx, Pageable pageable);
	Optional<WsPsl> findByIdAndOwnerIdx(Long id, Long ownerIdx);
	long countByOwnerIdx(Long ownerIdx);
}
