package com.wsproject.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wsproject.userservice.domain.User;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {

}
