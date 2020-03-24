package com.wsproject.authsvr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@RefreshScope
@EnableAspectJAutoProxy
@EnableJpaAuditing
@SpringBootApplication
public class AuthsvrApplication {	
	public static void main(String[] args) {
		SpringApplication.run(AuthsvrApplication.class, args);
	}
}
