package com.wsproject.batchservice.job;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wsproject.batchservice.domain.enums.WsType;
import com.wsproject.batchservice.dto.TodaysWsDto;
import com.wsproject.batchservice.dto.TokenInfo;
import com.wsproject.batchservice.job.reader.QueueItemReader;
import com.wsproject.batchservice.property.CustomProperties;
import com.wsproject.batchservice.service.RestService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class TodaysWsJob {

	private final JobBuilderFactory jobBuilderFactory;
	
	private final StepBuilderFactory stepBuilderFactory;
		
	private final CustomProperties properties;
	
	private final RestService restService;
	
	private TokenInfo tokenInfo;
	
	private long wsCount;
	
	@Bean
	public Job todayWsJob() throws Exception {
		return jobBuilderFactory.get("todayWsJob")
					.start(todayWsStep())
					.build();
	}
	
	@Bean
	public Step todayWsStep() throws Exception {
		return stepBuilderFactory.get("todayWsStep")
					.<Long, TodaysWsDto> chunk(1)
					.faultTolerant().retryLimit(10).retry(Exception.class)
					.reader(commonValueReader())
					.processor(todaysWsProcessor())
					.writer(todaysWsWriter())
					.build();
	}
	
	@Bean
	@StepScope
	public QueueItemReader<Long> commonValueReader() throws Exception {
		try {
			tokenInfo = restService.getTokenInfo();
			wsCount = Long.parseLong(restService.getForEntity(properties.getApiBaseUri() + "/ws-service/v1.0/wses/count", tokenInfo).getBody());
			long maxUserIdx = Long.parseLong(restService.getForEntity(properties.getApiBaseUri() + "/user-service/v1.0/users/maxIdx", tokenInfo).getBody());
			List<Long> userIdxList = LongStream.rangeClosed(1, maxUserIdx).mapToObj(Long::new).collect(Collectors.toList());
			
			return new QueueItemReader<Long>(userIdxList);
		} catch (Exception e) {
			log.info("commonValueReader failed");
			throw e;
		}
	}
	
	public ItemProcessor<Long, TodaysWsDto> todaysWsProcessor() {
		
		return new ItemProcessor<Long, TodaysWsDto>() {

			@Override
			public TodaysWsDto process(Long userIdx) throws Exception {
				long wsPslCount = Long.parseLong(restService.getForEntity(properties.getApiBaseUri() + "/ws-service/v1.0/users/" + userIdx + "/wses/count", tokenInfo).getBody());
				long randomNo = (long) (Math.random() * (wsCount + wsPslCount) + 1);
				ResponseEntity<String> entity;
				
				try {
					if(randomNo <= wsCount) {
						entity = restService.getForEntity(properties.getApiBaseUri() + "/ws-service/v1.0/wses/" + randomNo, tokenInfo);
					} else {
						entity = restService.getForEntity(properties.getApiBaseUri() + "/ws-service/v1.0/users/" + userIdx + "/wses/order/" + (randomNo - wsCount), tokenInfo);
					}
					
					String result = entity.getBody();
					JsonObject object = JsonParser.parseString(result).getAsJsonObject();
					TodaysWsDto todaysWs = TodaysWsDto.builder()
												.userIdx(userIdx)
												.content(object.get("content").getAsString())
												.author(object.get("author").getAsString())
												.type(WsType.valueOf(object.get("type").getAsString()))
												.build();
					
					return todaysWs;
					
				} catch (Exception e) {
					log.info("todaysWsProcessor failed at userIdx [{}]", userIdx);
					throw e;
				}
			}
		};
	}
	
	public ItemWriter<TodaysWsDto> todaysWsWriter() {
		
		return new ItemWriter<TodaysWsDto>() {

			@Override
			public void write(List<? extends TodaysWsDto> items) throws Exception {
				
				items.forEach(item -> {
					try {
						restService.postForEntity(properties.getApiBaseUri() + "/ws-service/v1.0/users/" + item.getUserIdx() + "/todaysWs", tokenInfo, item);
					} catch (Exception e) {
						log.info("todaysWsWriter failed at userIdx [{}]", item.getUserIdx());
						throw e;
					}
				});
			}
		};
	}
}
