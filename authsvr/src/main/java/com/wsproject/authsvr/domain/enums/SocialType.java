package com.wsproject.authsvr.domain.enums;

import static com.wsproject.authsvr.util.CommonUtil.convertObjToStr;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties.Registration;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

import com.wsproject.authsvr.domain.User;
import com.wsproject.authsvr.util.CustomOAuth2Provider;

import lombok.Getter;

/**
 * Social 서비스 종류 <br>
 * 사용자가 OAuth2를 통해 로그인할 수 있는 서비스 종류
 * @author mslim
 *
 */
@Getter
public enum SocialType {
	
	FACEBOOK("facebook", 0) {
		@Override
		public User convertUser(Map<String, Object> userAttr) {
			@SuppressWarnings("unchecked")
    		Object pictureUrl = ((HashMap<String, Object>) ((HashMap<String, Object>) userAttr.get("picture")).get("data")).get("url");
        	
        	return User.builder()
					.name(convertObjToStr(userAttr.get("name")))
					.email(convertObjToStr(userAttr.get("email")))
					.principal(convertObjToStr(requireNonNull(userAttr.get("id"))))
					.socialType(FACEBOOK)
					.pictureUrl(convertObjToStr(pictureUrl))
					.roles(singletonList(RoleType.USER.getValue()))
					.accountNonExpired(true)
					.accountNonLocked(true)
					.credentialsNonExpired(true)
					.enabled(true)
					.build();
		}

		@Override
		public ClientRegistration getRegistration(OAuth2ClientProperties oAuth2ClientProperties) {
			Registration registration = oAuth2ClientProperties.getRegistration().get(getRegistrationId());
			
			return CommonOAuth2Provider.FACEBOOK.getBuilder(getRegistrationId())
					.clientId(registration.getClientId())
					.clientSecret(registration.getClientSecret())
					// picture을 받아오기 위해 별도로 userInfoUri 설정
					.userInfoUri("https://graph.facebook.com/me?fields=id,name,email,picture")
					.build();
		}
	},
	GOOGLE("google", 1) {
		@Override
		public User convertUser(Map<String, Object> userAttr) {
			return User.builder()
					.name(convertObjToStr(userAttr.get("name")))
					.email(convertObjToStr(userAttr.get("email")))
					.principal(convertObjToStr(requireNonNull(userAttr.get(IdTokenClaimNames.SUB))))
					.socialType(GOOGLE)
					.pictureUrl(convertObjToStr(userAttr.get("picture")))
					.roles(singletonList(RoleType.USER.getValue()))
					.accountNonExpired(true)
					.accountNonLocked(true)
					.credentialsNonExpired(true)
					.enabled(true)
					.build();
		}

		@Override
		public ClientRegistration getRegistration(OAuth2ClientProperties oAuth2ClientProperties) {
			Registration registration = oAuth2ClientProperties.getRegistration().get(getRegistrationId());
			
			return CommonOAuth2Provider.GOOGLE.getBuilder(getRegistrationId())
					.clientId(registration.getClientId())
					.clientSecret(registration.getClientSecret())
					.build();
		}
	},
	KAKAO("kakao", 2) {
		@Override
		public User convertUser(Map<String, Object> userAttr) {
			@SuppressWarnings("unchecked")
    		Map<String, String> propertyMap = (HashMap<String, String>) userAttr.get("properties");
			
			return User.builder()
                    .name(propertyMap.get("nickname"))
                    .email(convertObjToStr(userAttr.get("kaccount_email")))
                    .principal(convertObjToStr(requireNonNull(userAttr.get("id"))))
                    .socialType(KAKAO)
                    .pictureUrl(propertyMap.get("thumbnail_image"))
                    .roles(singletonList(RoleType.USER.getValue()))
					.accountNonExpired(true)
					.accountNonLocked(true)
					.credentialsNonExpired(true)
					.enabled(true)
					.build();
		}

		@Override
		public ClientRegistration getRegistration(OAuth2ClientProperties oAuth2ClientProperties) {
			Registration registration = oAuth2ClientProperties.getRegistration().get(getRegistrationId());
			
			return CustomOAuth2Provider.KAKAO.getBuilder(getRegistrationId())
					.clientId(registration.getClientId())
					.build();
		}
	},
	GITHUB("github", 3) {
		@Override
		public User convertUser(Map<String, Object> userAttr) {
			return User.builder()
                    .name(convertObjToStr(userAttr.get("name")))
                    .email(convertObjToStr(userAttr.get("email")))
                    .principal(convertObjToStr(requireNonNull(userAttr.get("id"))))
                    .socialType(GITHUB)
                    .pictureUrl(convertObjToStr(userAttr.get("avatar_url")))
                    .roles(singletonList(RoleType.USER.getValue()))
					.accountNonExpired(true)
					.accountNonLocked(true)
					.credentialsNonExpired(true)
					.enabled(true)
					.build();
		}

		@Override
		public ClientRegistration getRegistration(OAuth2ClientProperties oAuth2ClientProperties) {
			Registration registration = oAuth2ClientProperties.getRegistration().get(getRegistrationId());
			
			return CommonOAuth2Provider.GITHUB.getBuilder(getRegistrationId())
					.clientId(registration.getClientId())
					.clientSecret(registration.getClientSecret())
					.build();
		}
	};
	
	private String registrationId;
	private int code;
	
	private static final Map<String, SocialType> registrationIdToEnum = Arrays.stream(values()).collect(toMap(SocialType::getRegistrationId, e -> e));
	private static final Map<Integer, SocialType> codeToEnum = Arrays.stream(values()).collect(toMap(SocialType::getCode, e -> e));
	
	private SocialType(String registrationId, int code) {
		this.registrationId = registrationId;
		this.code = code;
	}
	
	public static SocialType ofCode(int code) {
		return Optional.ofNullable(codeToEnum.get(code)).orElseThrow(() -> new IllegalArgumentException(String.format("상태코드에 code=[%s]가 존재하지 않습니다.", code)));
	}
	
	public static SocialType ofRegistrationId(String registrationId) {
		return Optional.ofNullable(registrationIdToEnum.get(registrationId))
				.orElseThrow(() -> new IllegalArgumentException(String.format("상태코드에 registrationId=[%s]가 존재하지 않습니다.", registrationId)));
	}
	
	/**
	 * SocialType별로 다른 형식으로 넘어오는 사용자 정보를 바탕으로 User 객체 생성
	 * @param userAttr
	 * @return
	 */
	public abstract User convertUser(Map<String, Object> userAttr);
	
	/**
	 * OAuth2 로그인 구성에 필요한 정보를 가져온다.
	 * @param oAuth2ClientProperties
	 * @return
	 */
	public abstract ClientRegistration getRegistration(OAuth2ClientProperties oAuth2ClientProperties);
}
