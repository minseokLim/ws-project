package com.wsproject.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wsproject.userservice.domain.User;
import com.wsproject.userservice.domain.enums.SocialType;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByPrincipalAndSocialType(String principal, SocialType socialType);
}
