package com.wsproject.batchservice.quartz;

import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import lombok.AllArgsConstructor;

/**
 * Quartz 속성
 * @author mslim
 *
 */
@AllArgsConstructor
@Configuration
public class QuartzConfig {
	
	private JobLauncher jobLauncher;
	
	private JobLocator jobLocator;
	
	@Bean
	public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
		JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
		jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
		
		return jobRegistryBeanPostProcessor;
	}
	
	@Bean
	public JobDetail todaysWsJobDetail() {	
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("jobName", "todaysWsJob");
		jobDataMap.put("jobLauncher", jobLauncher);
		jobDataMap.put("jobLocator", jobLocator);
		
		return JobBuilder.newJob(CustomQuartzJob.class)
					.withIdentity("todaysWsJob")
					.setJobData(jobDataMap)
					.storeDurably()
					.build();
	}
	
	@Bean
	public Trigger todaysWsJobTrigger() throws ParseException {		
		CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
		trigger.setJobDetail(todaysWsJobDetail());
		trigger.setCronExpression("10 0 0 * * ?"); // 매일 0시 10분으로 설정. TODO customProperties로 cron 속성을 뺄 예정
//		trigger.setCronExpression("0/5 * * * * ?");
		trigger.setDescription("todaysWsJobTrigger");
		trigger.afterPropertiesSet();
		
		return trigger.getObject();	
	}
	
	@Bean
	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean properties = new PropertiesFactoryBean();
		properties.setLocation(new ClassPathResource("/quartz.properties"));
		properties.afterPropertiesSet();
		
		return properties.getObject();
	}
	
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() throws IOException, ParseException {
		SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
		scheduler.setTriggers(todaysWsJobTrigger());
		scheduler.setQuartzProperties(quartzProperties());
		scheduler.setJobDetails(todaysWsJobDetail());
		
		return scheduler;
	}
}
