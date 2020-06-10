package com.wsproject.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wsproject.userservice.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "SELECT MAX(u.idx) FROM User u")
	Long getMaxUserIdx();
}
