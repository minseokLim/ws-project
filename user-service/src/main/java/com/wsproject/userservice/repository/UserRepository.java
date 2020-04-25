package com.wsproject.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wsproject.userservice.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
//	Optional<User> findByPrincipalAndSocialType(String principal, SocialType socialType);
	
	@Query(value = "SELECT IFNULL(MAX(idx), 0) AS MAX_IDX FROM TBL_USER", nativeQuery = true)
	Long getMaxUserIdx();
}
