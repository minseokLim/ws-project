package com.wsproject.clientsvr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@RefreshScope
@SpringBootApplication
public class ClientsvrApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientsvrApplication.class, args);
	}
}
