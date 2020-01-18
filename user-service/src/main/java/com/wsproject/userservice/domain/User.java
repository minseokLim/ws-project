package com.wsproject.userservice.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.wsproject.userservice.domain.enums.RoleType;
import com.wsproject.userservice.domain.enums.SocialType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//TODO need to optimize size of each column
@Entity
@Getter
@NoArgsConstructor
@Table(name = "TBL_USER", uniqueConstraints = @UniqueConstraint(columnNames={"email"}))
public class User extends BaseTimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;
	
	private String name;
	
	private String email;
	
	private String principal;
	
	private SocialType socialType;
	
	private String pictureUrl;
	
	private RoleType roleType;
	
	@Builder
	public User(String name, String email, String principal, SocialType socialType, String pictureUrl, RoleType roleType) {
		this.name = name;
		this.email = email;
		this.principal = principal;
		this.socialType = socialType;
		this.pictureUrl = pictureUrl;
		this.roleType = roleType;
	}
}
