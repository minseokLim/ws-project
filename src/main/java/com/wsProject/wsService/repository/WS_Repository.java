package com.wsProject.wsService.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wsProject.wsService.domain.WS;

@RepositoryRestResource
public interface WS_Repository extends JpaRepository<WS, Long> {
	
	@Query(value = "SELECT *, CASE WHEN B.ws_id IS NULL THEN 0 ELSE 1 END AS \"like\" "
				 + "FROM tbl_ws A LEFT JOIN tbl_like B "
				 + "ON B.user_email = ?1 AND A.id = B.ws_id "
				 + "WHERE A.by_admin = 1 OR A.owner_email = ?1", nativeQuery = true)
	Page<WS> findAllWithLike(Pageable pageable, String userEmail);
	
	@Query(value = "SELECT *, CASE WHEN B.ws_id IS NULL THEN 0 ELSE 1 END AS \"like\" "
			 	 + "FROM tbl_ws A LEFT JOIN tbl_like B "
			 	 + "ON B.user_email = ?2 AND A.id = B.ws_id "
			 	 + "WHERE A.id = ?1", nativeQuery = true)
	WS findByIdWithLike(Long id, String userEmail);
}
