package com.webdiario.eventhub.dto;

import com.webdiario.eventhub.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for event responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

    private Long id;
    private String eventId;
    private String eventType;
    private String source;
    private String version;
    private LocalDateTime eventTimestamp;
    private LocalDateTime processingTimestamp;
    private String correlationId;
    private String userId;
    private String tenantId;
    private String payload;
    private String schemaVersion;
    private Event.EventStatus status;
    private Event.EventPriority priority;
    private Integer processingAttempts;
    private String processingError;
    private String metadata;
    private String tags;
    private Long processingDuration;
    private Long payloadSize;
    private String hashPayload;
    private Boolean compressed;
    private Boolean encrypted;
    private String partitionKey;
    private String shardId;
    private Long sequenceNumber;
    private LocalDateTime expiresAt;
    private Integer retentionDays;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public static EventResponse fromEntity(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .eventId(event.getEventId())
                .eventType(event.getEventType())
                .source(event.getSource())
                .version(event.getVersion())
                .eventTimestamp(event.getEventTimestamp())
                .processingTimestamp(event.getProcessingTimestamp())
                .correlationId(event.getCorrelationId())
                .userId(event.getUserId())
                .tenantId(event.getTenantId())
                .payload(event.getPayload())
                .schemaVersion(event.getSchemaVersion())
                .status(event.getStatus())
                .priority(event.getPriority())
                .processingAttempts(event.getProcessingAttempts())
                .processingError(event.getProcessingError())
                .metadata(event.getMetadata())
                .tags(event.getTags())
                .processingDuration(event.getProcessingDuration())
                .payloadSize(event.getPayloadSize())
                .hashPayload(event.getHashPayload())
                .compressed(event.getCompressed())
                .encrypted(event.getEncrypted())
                .partitionKey(event.getPartitionKey())
                .shardId(event.getShardId())
                .sequenceNumber(event.getSequenceNumber())
                .expiresAt(event.getExpiresAt())
                .retentionDays(event.getRetentionDays())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .createdBy(event.getCreatedBy())
                .updatedBy(event.getUpdatedBy())
                .build();
    }
}
