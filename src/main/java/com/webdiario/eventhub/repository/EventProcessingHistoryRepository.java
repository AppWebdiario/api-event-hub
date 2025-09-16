package com.webdiario.eventhub.repository;

import com.webdiario.eventhub.entity.EventProcessingHistory;
import com.webdiario.eventhub.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for EventProcessingHistory entity
 */
@Repository
public interface EventProcessingHistoryRepository extends JpaRepository<EventProcessingHistory, Long> {

    List<EventProcessingHistory> findByEvent(Event event);
    
    List<EventProcessingHistory> findByEventOrderByAttemptNumberDesc(Event event);
    
    List<EventProcessingHistory> findByStatus(EventProcessingHistory.ProcessingStatus status);
    
    List<EventProcessingHistory> findByProcessorId(String processorId);
    
    List<EventProcessingHistory> findByProcessorType(String processorType);
    
    List<EventProcessingHistory> findByProcessingStartBetween(LocalDateTime start, LocalDateTime end);
    
    List<EventProcessingHistory> findByProcessingEndBetween(LocalDateTime start, LocalDateTime end);
    
    List<EventProcessingHistory> findByDurationMsGreaterThan(Long durationMs);
    
    List<EventProcessingHistory> findByDurationMsLessThan(Long durationMs);
    
    List<EventProcessingHistory> findByErrorCode(String errorCode);
    
    List<EventProcessingHistory> findByRetryCountGreaterThan(Integer retryCount);
    
    List<EventProcessingHistory> findByNextRetryBefore(LocalDateTime date);
    
    List<EventProcessingHistory> findByHostName(String hostName);
    
    List<EventProcessingHistory> findByThreadId(String threadId);
    
    List<EventProcessingHistory> findByCorrelationId(String correlationId);
    
    List<EventProcessingHistory> findByTraceId(String traceId);
    
    List<EventProcessingHistory> findBySpanId(String spanId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId ORDER BY eph.attemptNumber DESC")
    List<EventProcessingHistory> findByEventIdOrderByAttemptNumberDesc(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.eventId = :eventId ORDER BY eph.attemptNumber DESC")
    List<EventProcessingHistory> findByEventIdOrderByAttemptNumberDesc(@Param("eventId") String eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.eventType = :eventType")
    List<EventProcessingHistory> findByEventType(@Param("eventType") String eventType);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.source = :source")
    List<EventProcessingHistory> findBySource(@Param("source") String source);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.userId = :userId")
    List<EventProcessingHistory> findByUserId(@Param("userId") String userId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.tenantId = :tenantId")
    List<EventProcessingHistory> findByTenantId(@Param("tenantId") String tenantId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.errorMessage LIKE %:errorPattern%")
    List<EventProcessingHistory> findByErrorMessagePattern(@Param("errorPattern") String errorPattern);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.errorStackTrace LIKE %:stackTracePattern%")
    List<EventProcessingHistory> findByStackTracePattern(@Param("stackTracePattern") String stackTracePattern);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.processingMetadata LIKE %:metadataKey%")
    List<EventProcessingHistory> findByMetadataKey(@Param("metadataKey") String metadataKey);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.tags LIKE %:tag%")
    List<EventProcessingHistory> findByTag(@Param("tag") String tag);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.memoryUsageMb > :minMemory")
    List<EventProcessingHistory> findByMemoryUsageGreaterThan(@Param("minMemory") Long minMemory);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.cpuUsagePercent > :minCpu")
    List<EventProcessingHistory> findByCpuUsageGreaterThan(@Param("minCpu") Double minCpu);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.inputSizeBytes > :minSize")
    List<EventProcessingHistory> findByInputSizeGreaterThan(@Param("minSize") Long minSize);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.outputSizeBytes > :minSize")
    List<EventProcessingHistory> findByOutputSizeGreaterThan(@Param("minSize") Long minSize);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.inputPayloadHash = :hash")
    List<EventProcessingHistory> findByInputPayloadHash(@Param("hash") String hash);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.outputPayloadHash = :hash")
    List<EventProcessingHistory> findByOutputPayloadHash(@Param("hash") String hash);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId AND eph.status = :status")
    List<EventProcessingHistory> findByEventIdAndStatus(@Param("eventId") Long eventId, @Param("status") EventProcessingHistory.ProcessingStatus status);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId AND eph.attemptNumber = :attemptNumber")
    EventProcessingHistory findByEventIdAndAttemptNumber(@Param("eventId") Long eventId, @Param("attemptNumber") Integer attemptNumber);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId AND eph.status = 'SUCCESS'")
    EventProcessingHistory findSuccessfulProcessingByEventId(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId AND eph.status = 'FAILED' ORDER BY eph.attemptNumber DESC")
    List<EventProcessingHistory> findFailedProcessingsByEventId(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId AND eph.status = 'RETRY'")
    List<EventProcessingHistory> findRetryProcessingsByEventId(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId AND eph.status = 'TIMEOUT'")
    List<EventProcessingHistory> findTimeoutProcessingsByEventId(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId AND eph.status = 'CANCELLED'")
    List<EventProcessingHistory> findCancelledProcessingsByEventId(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId AND eph.status = 'STARTED'")
    List<EventProcessingHistory> findStartedProcessingsByEventId(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId AND eph.status = 'PROCESSING'")
    List<EventProcessingHistory> findProcessingByEventId(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId ORDER BY eph.processingStart DESC")
    List<EventProcessingHistory> findByEventIdOrderByProcessingStartDesc(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId ORDER BY eph.processingEnd DESC")
    List<EventProcessingHistory> findByEventIdOrderByProcessingEndDesc(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId ORDER BY eph.durationMs DESC")
    List<EventProcessingHistory> findByEventIdOrderByDurationDesc(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId ORDER BY eph.durationMs ASC")
    List<EventProcessingHistory> findByEventIdOrderByDurationAsc(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId ORDER BY eph.retryCount DESC")
    List<EventProcessingHistory> findByEventIdOrderByRetryCountDesc(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId ORDER BY eph.retryCount ASC")
    List<EventProcessingHistory> findByEventIdOrderByRetryCountAsc(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId ORDER BY eph.nextRetry ASC")
    List<EventProcessingHistory> findByEventIdOrderByNextRetryAsc(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId ORDER BY eph.memoryUsageMb DESC")
    List<EventProcessingHistory> findByEventIdOrderByMemoryUsageDesc(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId ORDER BY eph.cpuUsagePercent DESC")
    List<EventProcessingHistory> findByEventIdOrderByCpuUsageDesc(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId ORDER BY eph.inputSizeBytes DESC")
    List<EventProcessingHistory> findByEventIdOrderByInputSizeDesc(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId ORDER BY eph.outputSizeBytes DESC")
    List<EventProcessingHistory> findByEventIdOrderByOutputSizeDesc(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId ORDER BY eph.createdAt DESC")
    List<EventProcessingHistory> findByEventIdOrderByCreationDateDesc(@Param("eventId") Long eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId ORDER BY eph.createdAt ASC")
    List<EventProcessingHistory> findByEventIdOrderByCreationDateAsc(@Param("eventId") Long eventId);
    
    // Optimized queries based on composite indexes from Liquibase
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.event.id = :eventId AND eph.attemptNumber = :attemptNumber AND eph.status = :status")
    List<EventProcessingHistory> findByEventIdAndAttemptNumberAndStatus(@Param("eventId") Long eventId, @Param("attemptNumber") Integer attemptNumber, @Param("status") EventProcessingHistory.ProcessingStatus status);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.processorId = :processorId AND eph.status = :status AND eph.processingStart >= :startDate ORDER BY eph.processingStart DESC")
    List<EventProcessingHistory> findByProcessorIdAndStatusAndProcessingStart(@Param("processorId") String processorId, @Param("status") EventProcessingHistory.ProcessingStatus status, @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.status = :status AND eph.processingStart >= :startDate AND eph.processingEnd <= :endDate ORDER BY eph.processingStart DESC")
    List<EventProcessingHistory> findByStatusAndProcessingTimeRange(@Param("status") EventProcessingHistory.ProcessingStatus status, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.errorCode = :errorCode AND eph.status = :status AND eph.processingStart >= :startDate ORDER BY eph.processingStart DESC")
    List<EventProcessingHistory> findByErrorCodeAndStatusAndProcessingStart(@Param("errorCode") String errorCode, @Param("status") EventProcessingHistory.ProcessingStatus status, @Param("startDate") LocalDateTime startDate);
    
    // Performance monitoring queries
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.durationMs > :maxDuration ORDER BY eph.durationMs DESC")
    List<EventProcessingHistory> findSlowProcessingHistories(@Param("maxDuration") Long maxDuration);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.memoryUsageMb > :maxMemory ORDER BY eph.memoryUsageMb DESC")
    List<EventProcessingHistory> findHighMemoryUsageHistories(@Param("maxMemory") Long maxMemory);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.cpuUsagePercent > :maxCpu ORDER BY eph.cpuUsagePercent DESC")
    List<EventProcessingHistory> findHighCpuUsageHistories(@Param("maxCpu") Double maxCpu);
    
    // Error analysis queries
    @Query("SELECT eph.errorCode, COUNT(eph) FROM EventProcessingHistory eph WHERE eph.status = 'FAILED' AND eph.processingStart >= :startDate GROUP BY eph.errorCode ORDER BY COUNT(eph) DESC")
    List<Object[]> getErrorCodeCounts(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT eph.processorId, COUNT(eph) FROM EventProcessingHistory eph WHERE eph.status = 'FAILED' AND eph.processingStart >= :startDate GROUP BY eph.processorId ORDER BY COUNT(eph) DESC")
    List<Object[]> getProcessorFailureCounts(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT eph.processorType, COUNT(eph) FROM EventProcessingHistory eph WHERE eph.status = 'FAILED' AND eph.processingStart >= :startDate GROUP BY eph.processorType ORDER BY COUNT(eph) DESC")
    List<Object[]> getProcessorTypeFailureCounts(@Param("startDate") LocalDateTime startDate);
    
    // Performance analytics queries
    @Query("SELECT AVG(eph.durationMs) FROM EventProcessingHistory eph WHERE eph.status = 'SUCCESS' AND eph.durationMs IS NOT NULL AND eph.processingStart >= :startDate")
    Double getAverageProcessingTime(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT MAX(eph.durationMs) FROM EventProcessingHistory eph WHERE eph.status = 'SUCCESS' AND eph.durationMs IS NOT NULL AND eph.processingStart >= :startDate")
    Long getMaxProcessingTime(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT MIN(eph.durationMs) FROM EventProcessingHistory eph WHERE eph.status = 'SUCCESS' AND eph.durationMs IS NOT NULL AND eph.processingStart >= :startDate")
    Long getMinProcessingTime(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT AVG(eph.memoryUsageMb) FROM EventProcessingHistory eph WHERE eph.memoryUsageMb IS NOT NULL AND eph.processingStart >= :startDate")
    Double getAverageMemoryUsage(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT AVG(eph.cpuUsagePercent) FROM EventProcessingHistory eph WHERE eph.cpuUsagePercent IS NOT NULL AND eph.processingStart >= :startDate")
    Double getAverageCpuUsage(@Param("startDate") LocalDateTime startDate);
    
    // Health check queries
    @Query("SELECT COUNT(eph) FROM EventProcessingHistory eph WHERE eph.status = 'STARTED' AND eph.processingStart < :threshold")
    Long countStuckStartedProcessings(@Param("threshold") LocalDateTime threshold);
    
    @Query("SELECT COUNT(eph) FROM EventProcessingHistory eph WHERE eph.status = 'PROCESSING' AND eph.processingStart < :threshold")
    Long countStuckProcessingHistories(@Param("threshold") LocalDateTime threshold);
    
    @Query("SELECT COUNT(eph) FROM EventProcessingHistory eph WHERE eph.status = 'FAILED' AND eph.retryCount >= 3")
    Long countPermanentlyFailedProcessings();
    
    // Cleanup queries
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.processingStart < :cutoffDate AND eph.status IN ('SUCCESS', 'FAILED', 'CANCELLED')")
    List<EventProcessingHistory> findOldCompletedProcessings(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.processingStart < :cutoffDate AND eph.status = 'TIMEOUT'")
    List<EventProcessingHistory> findOldTimeoutProcessings(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    // Trace and correlation queries
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.traceId = :traceId ORDER BY eph.processingStart ASC")
    List<EventProcessingHistory> findByTraceIdOrderByProcessingStart(@Param("traceId") String traceId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.correlationId = :correlationId ORDER BY eph.processingStart ASC")
    List<EventProcessingHistory> findByCorrelationIdOrderByProcessingStart(@Param("correlationId") String correlationId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.spanId = :spanId ORDER BY eph.processingStart ASC")
    List<EventProcessingHistory> findBySpanIdOrderByProcessingStart(@Param("spanId") String spanId);
}
