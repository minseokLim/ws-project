package com.wsproject.authsvr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wsproject.authsvr.domain.AccessLog;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

}
