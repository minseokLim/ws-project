package com.wsproject.authsvr.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wsproject.authsvr.domain.enums.SocialType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "TBL_USER_AUTH", uniqueConstraints = {@UniqueConstraint(columnNames = {"principal", "socialType"})})
public class User extends BaseTimeEntity implements UserDetails {
	
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
	@CollectionTable(name = "TBL_USER_ROLES_AUTH")
    private List<String> roles = new ArrayList<String>();

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	boolean accountNonExpired;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	boolean accountNonLocked;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	boolean credentialsNonExpired;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	boolean enabled;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public String getUsername() {
		return String.valueOf(this.idx);
	}
	
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
