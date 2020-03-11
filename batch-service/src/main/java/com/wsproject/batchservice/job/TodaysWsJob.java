package com.wsproject.batchservice.job;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.wsproject.batchservice.property.CustomProperties;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
public class TodaysWsJob {

	private JobBuilderFactory jobBuilderFactory;
	
	private StepBuilderFactory stepBuilderFactory;
	
	private CustomProperties customProperties;
	
	private RestTemplate restTemplate;
	
	@Bean
	public Step step() {
		return stepBuilderFactory.get("step")
				.tasklet((contribution, chunkContext) ->{
					
					return RepeatStatus.FINISHED;
				}).build();
	}
}
