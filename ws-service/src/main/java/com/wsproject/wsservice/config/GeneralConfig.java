package com.wsproject.wsservice.config;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
/**
 * @author mslim
 * Bean 생성을 한 곳에서 관리하기 위한 Configuration
 */
import org.springframework.web.client.RestTemplate;

import com.wsproject.wsservice.util.AccessTokenInterceptor;

/**
 * @author mslim
 * Bean 생성을 한 곳에서 관리하기 위한 Configuration
 */
@Configuration
public class GeneralConfig {
	
	
	/**
	 * 토큰 전파를 위한 인터셉터가 추가된 RestTemplate
	 * @return RestTemplate
	 */
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate template = new RestTemplate();
		List<ClientHttpRequestInterceptor> interceptors = template.getInterceptors();
		
		if(interceptors == null) {
			template.setInterceptors(Collections.singletonList(new AccessTokenInterceptor()));
		} else {
			interceptors.add(new AccessTokenInterceptor());
			template.setInterceptors(interceptors);
		}
		
		return template;
	}

//	@Bean
//	public CommandLineRunner runner(WsRepository wsRepository, WsPslRepository wsPersonalRepository) {
//		return args -> {		
//			for(int i = 1; i <= 200; i++) {
//				wsRepository.save(Ws.builder()
//						.author("임민석" + i)
//						.content("차근차근히 해나가다 보면 잘 될거다. 날 받아주는 곳 한 곳쯤은 있겠지." + i)
//						.type(WsType.SELF_DEV)
//						.build()
//				);
//				
//				wsPersonalRepository.save(WsPsl.builder()
//						.author("개인작가" + i)
//						.content("너무 완벽하게 하려고 하지 말자" + i)
//						.ownerIdx((long) (i % 5 + 1))
//						.type(WsType.YOLO)
//						.build()
//				);
//			}
//		};
//	}
}
