package com.wsproject.batchservice.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wsproject.batchservice.domain.enums.WsType;
import com.wsproject.batchservice.dto.TodaysWsDto;
import com.wsproject.batchservice.dto.TokenInfo;
import com.wsproject.batchservice.property.CustomProperties;
import com.wsproject.batchservice.service.RestService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
public class TodaysWsJob {

	private JobBuilderFactory jobBuilderFactory;
	
	private StepBuilderFactory stepBuilderFactory;
		
	private CustomProperties properties;
	
	private RestService restService;
	
	@Bean
	public Job todayWsJob() {
		return jobBuilderFactory.get("todayWsJob").start(todayWsStep()).build();
	}
	
	@Bean
	public Step todayWsStep() {
		return stepBuilderFactory.get("todayWsStep").tasklet((contribution, chunkContext) ->{
			TokenInfo tokenInfo = restService.getTokenInfo();
			long maxUserIdx = Long.parseLong(restService.getForEntity(properties.getApiBaseUri() + "/user-service/v1.0/users/maxIdx", tokenInfo).getBody());
			long wsCount = Long.parseLong(restService.getForEntity(properties.getApiBaseUri() + "/ws-service/v1.0/wses/count", tokenInfo).getBody());
			
			for(long i = 1; i <= maxUserIdx; i++) {
				long wsPslCount = Long.parseLong(restService.getForEntity(properties.getApiBaseUri() + "/ws-service/v1.0/users/" + i + "/wses/count", tokenInfo).getBody());
				long randomNo = (long) (Math.random() * (wsCount + wsPslCount) + 1);
				ResponseEntity<String> entity;
				
				if(randomNo <= wsCount) {
					entity = restService.getForEntity(properties.getApiBaseUri() + "/ws-service/v1.0/wses/" + randomNo, tokenInfo);
				} else {
					entity = restService.getForEntity(properties.getApiBaseUri() + "/ws-service/v1.0/users/" + i + "/wses/" + (randomNo - wsCount), tokenInfo);
				}
				
				if(entity.getStatusCode().is2xxSuccessful()) {
					String result = entity.getBody();
					JsonObject object = JsonParser.parseString(result).getAsJsonObject();
					TodaysWsDto todaysWs = TodaysWsDto.builder()
												.userIdx(i)
												.content(object.get("content").getAsString())
												.author(object.get("author").getAsString())
												.type(WsType.valueOf(object.get("type").getAsString()))
												.build();
										
					entity = restService.postForEntity(properties.getApiBaseUri() + "/ws-service/v1.0/users/" + i + "/todaysWs", tokenInfo, todaysWs);
										
					if(entity.getStatusCode().isError()) {
						throw new Exception("todayWsStep failed");
					}
				} else {
					throw new Exception("todayWsStep failed");
				}
			}
			
			return RepeatStatus.FINISHED;
		}).build();
	}
}
