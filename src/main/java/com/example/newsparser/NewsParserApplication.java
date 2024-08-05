package com.example.newsparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for the News Parser application.
 * <p>
 * This is the entry point for the Spring Boot application. It includes:
 * - The @SpringBootApplication annotation to enable autoconfiguration, component scanning, and configuration.
 * - The @EnableScheduling annotation to enable scheduling tasks in the application.
 * <p>
 * Scheduling is configured to run tasks every 20 minutes using cron expressions.
 */
@EnableScheduling
@SpringBootApplication
public class NewsParserApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsParserApplication.class, args);
	}

}
