package com.wsproject.wsservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wsproject.wsservice.domain.WS;

public interface WS_Repository extends JpaRepository<WS, Long> {
	
	@Query("SELECT w "
		 + "FROM WS w "
		 + "LEFT JOIN w.like l "
		 + "WHERE l.userEmail = :userEmail")
	Page<WS> findWSesByUserEmail(Pageable pageable, String userEmail);
}
