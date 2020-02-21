package com.wsproject.authsvr.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wsproject.authsvr.domain.enums.SocialType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User implements UserDetails {
	
	private static final long serialVersionUID = 6510441028359513508L;

	private Long idx;
	
	private String name;
	
	private String email;
	
	private String principal;
	
	private SocialType socialType;
	
	private String pictureUrl;
	
	private String uid;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

    private List<String> roles = new ArrayList<String>();

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	boolean accountNonExpired;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	boolean accountNonLocked;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	boolean credentialsNonExpired;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	boolean enabled;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime modifiedDate;
	
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
	public User(Long idx, String name, String email, String principal, SocialType socialType, String pictureUrl,
			String uid, String password, List<String> roles, boolean accountNonExpired, boolean accountNonLocked,
			boolean credentialsNonExpired, boolean enabled, LocalDateTime createdDate, LocalDateTime modifiedDate) {
		this.idx = idx;
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
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}
}
