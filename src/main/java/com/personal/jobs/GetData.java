package com.personal.jobs;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.personal.tasklet.GetSlotTasklet;

@Configuration
@EnableBatchProcessing
public class GetData {
	
	@Autowired
	private SimpleJobLauncher jobLauncher;
	
	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;
	
	@Autowired
	GetSlotTasklet getSlotTasklet;
	
	@Scheduled(cron = "${get.slot.call.interval}")
	public void perform() throws Exception {

		System.out.println("Job Started at :" + new Date());

		JobParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();

		JobExecution execution = jobLauncher.run(sendTaskNotificationJob(), param);

		System.out.println("Job finished with status :" + execution.getStatus());
	}
	
	@Bean 
    public Job sendTaskNotificationJob() {
        return jobs
          .get("sendThresholdNotificationJob")
          .start(getSlots())
          .build();
    }
	
	@Bean
    protected Step getSlots() {
        return steps
          .get("getAboepDataStep")
          .tasklet(getSlotTasklet)
          .build();
    }
	
}
