package com.wsproject.userservice.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wsproject.userservice.domain.enums.SocialType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "TBL_USER", indexes = {@Index(columnList = "socialType,principal")})
public class User extends BaseTimeEntity implements Serializable {

	private static final long serialVersionUID = 6510441028359513508L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idx;
	
	@Column(nullable = false)
	private String name;
	
	private String email;
	
	@Column(length = 100)
	private String principal;
	
	@Column(length = 2)
	private SocialType socialType;
	
	private String pictureUrl;
	
	@Column(unique = true)
	private String uid;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(length = 100)
	private String password;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "TBL_USER_ROLES")
    private List<String> roles = new ArrayList<String>();

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	boolean accountNonExpired;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	boolean accountNonLocked;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	boolean credentialsNonExpired;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	boolean enabled;

	@Builder
	public User(String name, String email, String principal, SocialType socialType, String pictureUrl, String uid,
				String password, List<String> roles, boolean accountNonExpired, boolean accountNonLocked,
				boolean credentialsNonExpired, boolean enabled) {
		this.name = name;
		this.email = email;
		this.principal = principal;
		this.socialType = socialType;
		this.pictureUrl = pictureUrl;
		this.uid = uid;
		this.password = password;
		this.roles = roles;
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
	}
}
