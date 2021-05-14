package com.personal.config;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class BatchConfig {

	@Value("${batch.core.pool.size}")
	int corePoolSize;
	
	@Value("${batch.max.pool.size}")
	int maxPoolSize;
	
	@Value("${batch.queue.capacity}")
	int queueCapacity;
	
	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(corePoolSize);
		taskExecutor.setMaxPoolSize(maxPoolSize);
		taskExecutor.setQueueCapacity(queueCapacity);
		return taskExecutor;
	}
	
	@Bean
    public SimpleJobLauncher asyncJobLauncher(ThreadPoolTaskExecutor taskExecutor, JobRepository jobRepository){
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.setJobRepository(jobRepository);
        return jobLauncher;
    }
	
}
