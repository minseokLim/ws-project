package com.wsproject.wsservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.wsproject.wsservice.domain.WS;
import com.wsproject.wsservice.domain.enums.WS_Type;
import com.wsproject.wsservice.repository.WS_Repository;

@EnableJpaAuditing
//@EnableAspectJAutoProxy
@SpringBootApplication
public class WS_ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WS_ServiceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner runner(WS_Repository repository) {
		return args -> {
			
			for(int i = 1; i <= 200; i++) {
				repository.save(WS.builder()
						.author("임민석" + i)
						.content("차근차근히 해나가다 보면 잘 될거다. 날 받아주는 곳 한 곳쯤은 있겠지." + i)
						.byAdmin(true)
						.type(WS_Type.SELF_DEV)
						.ownerEmail("mslim" + i + "@naver.com")
						.build()
				);
			}
		};
	}
}
