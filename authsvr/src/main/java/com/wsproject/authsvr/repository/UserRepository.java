package com.wsproject.authsvr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wsproject.authsvr.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUid(String email);
}
