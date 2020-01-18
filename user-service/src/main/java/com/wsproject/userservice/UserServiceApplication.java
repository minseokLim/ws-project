package com.wsproject.userservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.wsproject.userservice.domain.User;
import com.wsproject.userservice.domain.enums.RoleType;
import com.wsproject.userservice.domain.enums.SocialType;
import com.wsproject.userservice.repository.UserRepository;

@SpringBootApplication
@RefreshScope
@EnableJpaAuditing
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner runner(UserRepository repository) {
		return args -> {
			for(int i = 1; i <= 100; i++) {
				int mod = i % 3;
				SocialType socialType;
				
				switch (mod) {
				case 1:
					socialType = SocialType.FACEBOOK;
					break;
				case 2:
					socialType = SocialType.GOOGLE;
					break;
				default:
					socialType = SocialType.KAKAO;
					break;
				}
				
				repository.save(User.builder()
								.name("minseok" + i)
								.email("mslim" + i + "@naver.com")
								.principal(String.valueOf(i))
								.socialType(socialType)
								.pictureUrl("")
								.roleType(RoleType.USER)
								.build());
			}
		};
	}
}
