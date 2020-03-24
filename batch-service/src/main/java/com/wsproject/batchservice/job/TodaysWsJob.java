package com.wsproject.batchservice.job;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wsproject.batchservice.dto.TokenInfo;
import com.wsproject.batchservice.job.reader.QueueItemReader;
import com.wsproject.batchservice.util.RestUtil;
import com.wsproject.batchservice.util.TokenUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class TodaysWsJob {

	private final JobBuilderFactory jobBuilderFactory;
	
	private final StepBuilderFactory stepBuilderFactory;
		
	private final TokenUtil tokenUtil;
	
	private TokenInfo tokenInfo;
		
	@Bean
	public Job todayWsJob() throws Exception {
		return jobBuilderFactory.get("todayWsJob")
					.start(todayWsStep())
					.build();
	}
	
	@Bean
	public Step todayWsStep() throws Exception {
		return stepBuilderFactory.get("todayWsStep")
					.<Long, Long> chunk(1)
					.faultTolerant().retryLimit(10).retry(Exception.class)
					.reader(todayWsReader())
					.writer(todaysWsWriter())
					.build();
	}
	
	@Bean
	@StepScope
	public QueueItemReader<Long> todayWsReader() throws Exception {
		try {
			tokenInfo = tokenUtil.getTokenInfo();
			RestUtil restUtil = RestUtil.builder().url("/user-service/v1.0/users/maxIdx").get().tokenInfo(tokenInfo).build();
			long maxUserIdx = Long.parseLong(restUtil.exchange().getBody());
			List<Long> userIdxList = LongStream.rangeClosed(1, maxUserIdx).mapToObj(Long::new).collect(Collectors.toList());
			
			return new QueueItemReader<Long>(userIdxList);
		} catch (Exception e) {
			log.info("todayWsReader failed");
			throw e;
		}
	}
	
	public ItemWriter<Long> todaysWsWriter() {
		
		return new ItemWriter<Long>() {

			@Override
			public void write(List<? extends Long> items) throws Exception {
				items.forEach(userIdx -> {
					try {
						RestUtil restUtil = RestUtil.builder().url("/ws-service/v1.0/users/" + userIdx + "/todaysWs").post().tokenInfo(tokenInfo).build();
						restUtil.exchange();
					} catch (Exception e) {
						log.info("todaysWsWriter failed at userIdx [{}]", userIdx);
						throw e;
					}
				});
			}
		};
	}
}
