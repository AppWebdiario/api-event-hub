package com.webdiario.eventhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import br.com.tiagoramos.webdiario.security.starter.EnableWebdiarioSecurity;

/**
 * Main application class for WebDiario Event Hub
 * Provides event processing, storage and distribution using RabbitMQ
 */
@SpringBootApplication
@EntityScan("com.webdiario.eventhub.entity")
@EnableJpaRepositories("com.webdiario.eventhub.repository")
@EnableAsync
@EnableScheduling
@EnableWebdiarioSecurity
public class EventHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventHubApplication.class, args);
    }
}
