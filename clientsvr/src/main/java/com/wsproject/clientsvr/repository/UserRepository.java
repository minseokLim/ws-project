package com.wsproject.clientsvr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wsproject.clientsvr.domain.User;
import com.wsproject.clientsvr.domain.enums.SocialType;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByPrincipalAndSocialType(String principal, SocialType socialType);	
}
