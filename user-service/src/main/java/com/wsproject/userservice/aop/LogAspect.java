package com.wsproject.userservice.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LogAspect {
	
	@Around("execution(* com.wsproject.userservice.controller.*.*(..))")
	public Object startEndLog(ProceedingJoinPoint point) throws Throwable {
		Signature signature = point.getSignature();
		
		log.info(signature.getName() + " started - " + signature.getDeclaringTypeName());
		
		Object result = point.proceed();
		
		log.info(signature.getName() + " ended - " + signature.getDeclaringTypeName());
		
		return result;
	}
}