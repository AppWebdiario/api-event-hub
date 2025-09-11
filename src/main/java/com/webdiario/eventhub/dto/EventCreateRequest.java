package com.webdiario.eventhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for creating new events
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateRequest {

    @NotBlank(message = "Event type is required")
    @Size(max = 100, message = "Event type must not exceed 100 characters")
    private String eventType;

    @NotBlank(message = "Event source is required")
    @Size(max = 100, message = "Event source must not exceed 100 characters")
    private String source;

    @NotBlank(message = "Event version is required")
    @Size(max = 20, message = "Event version must not exceed 20 characters")
    private String version;

    @NotNull(message = "Event timestamp is required")
    private LocalDateTime eventTimestamp;

    @Size(max = 100, message = "Correlation ID must not exceed 100 characters")
    private String correlationId;

    @Size(max = 100, message = "User ID must not exceed 100 characters")
    private String userId;

    @Size(max = 100, message = "Tenant ID must not exceed 100 characters")
    private String tenantId;

    @NotBlank(message = "Event payload is required")
    private String payload;

    @Size(max = 20, message = "Schema version must not exceed 20 characters")
    private String schemaVersion;

    private EventPriority priority;

    @Size(max = 500, message = "Tags must not exceed 500 characters")
    private String tags;

    private Map<String, Object> metadata;

    @Size(max = 100, message = "Partition key must not exceed 100 characters")
    private String partitionKey;

    private Integer retentionDays;

    @Size(max = 100, message = "Created by must not exceed 100 characters")
    private String createdBy;

    public enum EventPriority {
        LOW, NORMAL, HIGH, URGENT, CRITICAL
    }
}
