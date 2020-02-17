package com.wsproject.authsvr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wsproject.authsvr.domain.User;
import com.wsproject.authsvr.domain.enums.SocialType;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {
	User findByPrincipalAndSocialType(String principal, SocialType socialType);
	Optional<User> findByUid(String uid);
}
