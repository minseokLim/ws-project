package com.wsproject.authsvr.domain;

import java.time.LocalDateTime;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

/**
 * 생성/수정 시간을 JPA Auditing을 통해 자동 생성해주기 위한 class <br>
 * 이 클래스를 상속받는 domain클래스는 생성/수정 시 자동으로 생성/수정 시간이 DB에 저장된다.
 * @author mslim
 *
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {
	
	@CreatedDate
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	private LocalDateTime modifiedDate;
}
