package com.wsproject.authsvr.domain;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보를 담는 class
 * @author mslim
 *
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TBL_USER", uniqueConstraints = {@UniqueConstraint(columnNames = {"principal", "socialType"})})
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
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream().map(SimpleGrantedAuthority::new).collect(toList());
	}
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public String getUsername() {
		return String.valueOf(this.idx);
	}
	
	@Builder
	private User(String name, String email, String principal, SocialType socialType, String pictureUrl, String uid,
				String password, List<String> roles, boolean accountNonExpired, boolean accountNonLocked,
				boolean credentialsNonExpired, boolean enabled) {
		this.name = Objects.requireNonNull(name);
		this.email = email;
		this.principal = principal;
		this.socialType = socialType;
		this.pictureUrl = pictureUrl;
		this.uid = uid;
		this.password = password;
		this.roles = Objects.requireNonNull(roles);
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
	}
	
	@Override
	public String toString() {
		return "User [idx=" + idx + ", name=" + name + ", email=" + email + ", principal=" + principal + ", socialType="
				+ socialType + ", uid=" + uid + ", roles=" + roles + ", accountNonExpired=" + accountNonExpired 
				+ ", accountNonLocked=" + accountNonLocked + ", credentialsNonExpired=" + credentialsNonExpired + ", enabled=" + enabled + "]";
	}
}
