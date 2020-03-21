package com.wsproject.wsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableAspectJAutoProxy
@RefreshScope
@SpringBootApplication
public class WsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WsServiceApplication.class, args);
	}
}
