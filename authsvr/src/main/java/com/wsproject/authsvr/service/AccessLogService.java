package com.wsproject.authsvr.service;

import org.springframework.stereotype.Service;

import com.wsproject.authsvr.domain.AccessLog;
import com.wsproject.authsvr.repository.AccessLogRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccessLogService {

	private AccessLogRepository accessLogRepository;
	
	public AccessLog save(AccessLog accessLog) {
		return accessLogRepository.save(accessLog.setNowOnAccessDate());
	}
}
