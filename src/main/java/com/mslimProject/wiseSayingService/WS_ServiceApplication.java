package com.mslimProject.wiseSayingService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.mslimProject.wiseSayingService.domain.WiseSaying;
import com.mslimProject.wiseSayingService.domain.enums.WS_Type;
import com.mslimProject.wiseSayingService.repository.WS_Repository;

@EnableJpaAuditing
@SpringBootApplication
public class WS_ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WS_ServiceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner runner(WS_Repository repository) {
		return args -> {
			WiseSaying saying = WiseSaying.builder()
									.author("찰리 채플린")
									.content("진정으로 웃으려면 고통을 참아야 하며, 나아가 고통을 즐길 줄 알아야 한다.")
									.byAdmin(true)
									.type(WS_Type.SELF_DEV)
									.ownerId("admin")
									.build();
			
			repository.save(saying);
		};
	}
}
