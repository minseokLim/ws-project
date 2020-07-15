package com.wsproject.batchservice;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.wsproject.batchservice.util.ImmutableRestTemplate;

@EnableBatchProcessing
@EnableAspectJAutoProxy
@SpringBootApplication
public class BatchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchServiceApplication.class, args);
	}
	
	@Bean
	public RestOperations restTemplate() {
		return new ImmutableRestTemplate(new RestTemplate());
	}
}
