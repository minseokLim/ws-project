package com.wsproject.batchservice.quartz;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomQuartzJob extends QuartzJobBean {

	private String jobName;
	
	private JobLauncher jobLauncher;
	
	private JobLocator jobLocator;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		
		try {
			Job job = jobLocator.getJob(jobName);
			
			Date today = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String jobID = df.format(today); // jobID를 오늘의 날짜로 설정함으로써, 하루에 한 번만 실행이 가능하도록 한다.
//			String jobID = String.valueOf(System.currentTimeMillis());
			
			JobParameters params = new JobParametersBuilder().addString("JobID", jobID).toJobParameters();
			
			jobLauncher.run(job, params);
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}
}
