package com.wsproject.batchservice.job;

import static java.util.stream.Collectors.toList;

import java.util.List;
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
public class BatchJob {

	private final JobBuilderFactory jobBuilderFactory;
	
	private final StepBuilderFactory stepBuilderFactory;
		
	private final TokenUtil tokenUtil;
	
	private TokenInfo tokenInfo;
		
	@Bean
	public Job todaysWsJob() throws Exception {
		return jobBuilderFactory.get("todaysWsJob")
					.start(todaysWsStep())
					.build();
	}
	
	@Bean
	public Step todaysWsStep() throws Exception {
		return stepBuilderFactory.get("todaysWsStep")
					.<Long, Long> chunk(1) // DB를 통해 배치 실행을 하는 것이 아니기 때문에 chunk사이즈는 의미가 없다.
					.faultTolerant().retryLimit(10).retry(Exception.class) // 배치 실패 시에도 10번까지는 재시도를 한다.
					.reader(todaysWsReader())
					.writer(todaysWsWriter())
					.build();
	}
	
	@Bean
	@StepScope
	public QueueItemReader<Long> todaysWsReader() throws Exception {
		try {
			tokenInfo = tokenUtil.getTokenInfo();
			RestUtil restUtil = RestUtil.builder().url("/user-service/v1.0/users/maxIdx").tokenInfo(tokenInfo).build();
			long maxUserIdx = restUtil.exchange(Long.class).getBody(); // auto increment로 설정되어있는 userIdx의 최대값을 구한다.
			List<Long> userIdxList = LongStream.rangeClosed(1, maxUserIdx).mapToObj(Long::new).collect(toList());
			
			return new QueueItemReader<Long>(userIdxList);
		} catch (Exception e) {
			log.info("todaysWsReader failed");
			throw e;
		}
	}
	
	public ItemWriter<Long> todaysWsWriter() {
		
		return new ItemWriter<Long>() {

			@Override
			public void write(List<? extends Long> items) throws Exception {
				items.forEach(userIdx -> {
					try {
						// 사용자의 오늘의 명언을 리프레쉬한다.
						RestUtil restUtil = RestUtil.builder().url("/ws-service/v1.0/users/" + userIdx + "/todaysWs").tokenInfo(tokenInfo).build();
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
