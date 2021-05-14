package com.personal.alert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackages = { "com.personal.*" })
@EntityScan("com.personal.*")
@EnableCaching
@PropertySource(value = "file:properties/application.properties", ignoreResourceNotFound = true)
public class AlertApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlertApplication.class, args);
	}

}
