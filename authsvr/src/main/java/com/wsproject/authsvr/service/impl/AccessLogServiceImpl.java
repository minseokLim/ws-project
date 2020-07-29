package com.wsproject.authsvr.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wsproject.authsvr.domain.AccessLog;
import com.wsproject.authsvr.repository.AccessLogRepository;
import com.wsproject.authsvr.service.AccessLogService;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class AccessLogServiceImpl implements AccessLogService {

	private AccessLogRepository accessLogRepository;
	
	@Override
	public AccessLog insertAccessLog(AccessLog accessLog) {
		return accessLogRepository.save(accessLog);
	}
}
