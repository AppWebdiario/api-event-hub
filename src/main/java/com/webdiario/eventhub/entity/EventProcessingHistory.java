package com.webdiario.eventhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity for storing event processing history
 */
@Entity
@Table(name = "event_processing_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventProcessingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Event is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber;

    @Column(name = "processor_id", length = 100)
    private String processorId;

    @Column(name = "processor_type", length = 50)
    private String processorType;

    @Column(name = "processing_start", nullable = false)
    private LocalDateTime processingStart;

    @Column(name = "processing_end")
    private LocalDateTime processingEnd;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ProcessingStatus status;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "error_stack_trace", columnDefinition = "TEXT")
    private String errorStackTrace;

    @Column(name = "error_code", length = 50)
    private String errorCode;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "next_retry")
    private LocalDateTime nextRetry;

    @Column(name = "processing_metadata", columnDefinition = "JSON")
    private String processingMetadata;

    @Column(name = "input_payload_hash", length = 64)
    private String inputPayloadHash;

    @Column(name = "output_payload_hash", length = 64)
    private String outputPayloadHash;

    @Column(name = "input_size_bytes")
    private Long inputSizeBytes;

    @Column(name = "output_size_bytes")
    private Long outputSizeBytes;

    @Column(name = "memory_usage_mb")
    private Long memoryUsageMb;

    @Column(name = "cpu_usage_percent")
    private Double cpuUsagePercent;

    @Column(name = "thread_id", length = 50)
    private String threadId;

    @Column(name = "host_name", length = 100)
    private String hostName;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "correlation_id", length = 100)
    private String correlationId;

    @Column(name = "trace_id", length = 100)
    private String traceId;

    @Column(name = "span_id", length = 100)
    private String spanId;

    @Column(name = "tags", length = 500)
    private String tags;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (processingStart == null) {
            processingStart = LocalDateTime.now();
        }
    }

    // Helper methods
    public void setProcessingEnd(LocalDateTime end) {
        this.processingEnd = end;
        if (this.processingStart != null) {
            this.durationMs = java.time.Duration.between(this.processingStart, end).toMillis();
        }
    }

    public boolean isSuccessful() {
        return status == ProcessingStatus.SUCCESS;
    }

    public boolean isFailed() {
        return status == ProcessingStatus.FAILED;
    }

    public boolean isRetryable() {
        return status == ProcessingStatus.FAILED && retryCount < 3;
    }

    public enum ProcessingStatus {
        STARTED, PROCESSING, SUCCESS, FAILED, CANCELLED, TIMEOUT, RETRY
    }
}
