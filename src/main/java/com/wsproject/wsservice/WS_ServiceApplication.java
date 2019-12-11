package com.wsproject.wsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WS_ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WS_ServiceApplication.class, args);
	}
}
