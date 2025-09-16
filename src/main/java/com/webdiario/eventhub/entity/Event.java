package com.webdiario.eventhub.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity for storing processed events
 */
@Entity
@Table(name = "events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Event ID is required")
    @Column(name = "event_id", unique = true, nullable = false, length = 100)
    private String eventId;

    @NotBlank(message = "Event type is required")
    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @NotBlank(message = "Event source is required")
    @Column(name = "source", nullable = false, length = 100)
    private String source;

    @NotBlank(message = "Event version is required")
    @Column(name = "version", nullable = false, length = 20)
    private String version;

    @NotNull(message = "Event timestamp is required")
    @Column(name = "event_timestamp", nullable = false)
    private LocalDateTime eventTimestamp;

    @Column(name = "processing_timestamp", nullable = false)
    @Builder.Default
    private LocalDateTime processingTimestamp = LocalDateTime.now();

    @Column(name = "correlation_id", length = 100)
    private String correlationId;

    @Column(name = "user_id", length = 100)
    private String userId;

    @Column(name = "tenant_id", length = 100)
    private String tenantId;

    @Column(name = "payload", columnDefinition = "JSON")
    private String payload;

    @Column(name = "schema_version", length = 20)
    private String schemaVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private EventStatus status = EventStatus.PROCESSED;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", length = 20)
    @Builder.Default
    private EventPriority priority = EventPriority.NORMAL;

    @Column(name = "processing_attempts")
    @Builder.Default
    private Integer processingAttempts = 0;

    @Column(name = "max_attempts")
    @Builder.Default
    private Integer maxAttempts = 3;

    @Column(name = "next_retry")
    private LocalDateTime nextRetry;

    @Column(name = "processing_error", columnDefinition = "TEXT")
    private String processingError;

    @Column(name = "metadata", columnDefinition = "JSON")
    private String metadata;

    @Column(name = "tags", length = 500)
    private String tags;

    @Column(name = "processing_duration")
    private Long processingDuration; // milliseconds

    @Column(name = "payload_size")
    private Long payloadSize; // bytes

    @Column(name = "hash_payload", length = 64)
    private String hashPayload;

    @Column(name = "compressed", nullable = false)
    @Builder.Default
    private Boolean compressed = false;

    @Column(name = "encrypted", nullable = false)
    @Builder.Default
    private Boolean encrypted = false;

    @Column(name = "partition_key", length = 100)
    private String partitionKey;

    @Column(name = "shard_id", length = 50)
    private String shardId;

    @Column(name = "sequence_number")
    private Long sequenceNumber;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "retention_days")
    @Builder.Default
    private Integer retentionDays = 90;

    // Audit fields
    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Helper methods
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean canRetry() {
        return processingAttempts < maxAttempts;
    }

    public boolean isRetryable() {
        return status == EventStatus.FAILED && canRetry();
    }

    public void incrementAttempts() {
        this.processingAttempts++;
    }

    public void setNextRetry(LocalDateTime nextRetry) {
        this.nextRetry = nextRetry;
    }

    public enum EventStatus {
        PENDING, PROCESSING, PROCESSED, FAILED, RETRY, CANCELLED, EXPIRED
    }

    public enum EventPriority {
        LOW, NORMAL, HIGH, URGENT, CRITICAL
    }
}
