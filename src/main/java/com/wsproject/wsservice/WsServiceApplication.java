package com.wsproject.wsservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.wsproject.wsservice.domain.Ws;
import com.wsproject.wsservice.domain.enums.WsType;
import com.wsproject.wsservice.repository.WsRepository;

@EnableJpaAuditing
//@EnableAspectJAutoProxy
@SpringBootApplication
public class WsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WsServiceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner runner(WsRepository repository) {
		return args -> {
			
			for(int i = 1; i <= 200; i++) {
				repository.save(Ws.builder()
						.author("임민석" + i)
						.content("차근차근히 해나가다 보면 잘 될거다. 날 받아주는 곳 한 곳쯤은 있겠지." + i)
						.byAdmin(true)
						.type(WsType.SELF_DEV)
						.ownerEmail("mslim" + i + "@naver.com")
						.build()
				);
			}
		};
	}
}
