package com.wsproject.batchservice.quartz;

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
			JobParameters params = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();
			jobLauncher.run(job, params);
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}
}
