package com.webdiario.eventhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for WebDiario Event Hub
 * Provides event processing, storage and distribution using AWS SQS and SNS
 */
@SpringBootApplication
@EntityScan("com.webdiario.eventhub.entity")
@EnableJpaRepositories("com.webdiario.eventhub.repository")
@EnableAsync
@EnableScheduling
public class EventHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventHubApplication.class, args);
    }
}
