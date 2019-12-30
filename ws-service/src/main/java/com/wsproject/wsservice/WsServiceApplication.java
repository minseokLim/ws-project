package com.wsproject.wsservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.wsproject.wsservice.domain.Ws;
import com.wsproject.wsservice.domain.WsPersonal;
import com.wsproject.wsservice.domain.enums.WsType;
import com.wsproject.wsservice.repository.WsPersonalRepository;
import com.wsproject.wsservice.repository.WsRepository;

@EnableJpaAuditing
//@EnableAspectJAutoProxy
@RefreshScope //  컨픽서버에서 구성정보(사용자 정의로 한정)가 바뀌었을 시, 구성정보를 업데이트 한다.
@SpringBootApplication
public class WsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WsServiceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner runner(WsRepository wsRepository, WsPersonalRepository wsPersonalRepository) {
		return args -> {		
			for(int i = 1; i <= 200; i++) {
				wsRepository.save(Ws.builder()
						.author("임민석" + i)
						.content("차근차근히 해나가다 보면 잘 될거다. 날 받아주는 곳 한 곳쯤은 있겠지." + i)
						.type(WsType.SELF_DEV)
						.build()
				);
				
				wsPersonalRepository.save(WsPersonal.builder()
						.author("개인작가" + i)
						.content("너무 완벽하게 하려고 하지 말자" + i)
						.ownerEmail("mslim8803@naver.com")
						.type(WsType.YOLO)
						.build()
				);
			}
		};
	}
}
