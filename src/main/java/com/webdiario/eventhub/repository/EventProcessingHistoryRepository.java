package com.webdiario.eventhub.repository;

import com.webdiario.eventhub.entity.EventProcessingHistory;
import com.webdiario.eventhub.entity.Evento;
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

    List<EventProcessingHistory> findByEvento(Evento evento);
    
    List<EventProcessingHistory> findByEventoOrderByAttemptNumberDesc(Evento evento);
    
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
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId ORDER BY eph.attemptNumber DESC")
    List<EventProcessingHistory> findByEventoIdOrderByAttemptNumberDesc(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.eventId = :eventId ORDER BY eph.attemptNumber DESC")
    List<EventProcessingHistory> findByEventIdOrderByAttemptNumberDesc(@Param("eventId") String eventId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.tipoEvento = :tipoEvento")
    List<EventProcessingHistory> findByTipoEvento(@Param("tipoEvento") String tipoEvento);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.origem = :origem")
    List<EventProcessingHistory> findByOrigem(@Param("origem") String origem);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.userId = :userId")
    List<EventProcessingHistory> findByUserId(@Param("userId") String userId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.tenantId = :tenantId")
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
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId AND eph.status = :status")
    List<EventProcessingHistory> findByEventoIdAndStatus(@Param("eventoId") Long eventoId, @Param("status") EventProcessingHistory.ProcessingStatus status);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId AND eph.attemptNumber = :attemptNumber")
    EventProcessingHistory findByEventoIdAndAttemptNumber(@Param("eventoId") Long eventoId, @Param("attemptNumber") Integer attemptNumber);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId AND eph.status = 'SUCCESS'")
    EventProcessingHistory findSuccessfulProcessingByEventoId(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId AND eph.status = 'FAILED' ORDER BY eph.attemptNumber DESC")
    List<EventProcessingHistory> findFailedProcessingsByEventoId(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId AND eph.status = 'RETRY'")
    List<EventProcessingHistory> findRetryProcessingsByEventoId(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId AND eph.status = 'TIMEOUT'")
    List<EventProcessingHistory> findTimeoutProcessingsByEventoId(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId AND eph.status = 'CANCELLED'")
    List<EventProcessingHistory> findCancelledProcessingsByEventoId(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId AND eph.status = 'STARTED'")
    List<EventProcessingHistory> findStartedProcessingsByEventoId(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId AND eph.status = 'PROCESSING'")
    List<EventProcessingHistory> findProcessingByEventoId(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId ORDER BY eph.processingStart DESC")
    List<EventProcessingHistory> findByEventoIdOrderByProcessingStartDesc(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId ORDER BY eph.processingEnd DESC")
    List<EventProcessingHistory> findByEventoIdOrderByProcessingEndDesc(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId ORDER BY eph.durationMs DESC")
    List<EventProcessingHistory> findByEventoIdOrderByDurationDesc(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId ORDER BY eph.durationMs ASC")
    List<EventProcessingHistory> findByEventoIdOrderByDurationAsc(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId ORDER BY eph.retryCount DESC")
    List<EventProcessingHistory> findByEventoIdOrderByRetryCountDesc(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId ORDER BY eph.retryCount ASC")
    List<EventProcessingHistory> findByEventoIdOrderByRetryCountAsc(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId ORDER BY eph.nextRetry ASC")
    List<EventProcessingHistory> findByEventoIdOrderByNextRetryAsc(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId ORDER BY eph.memoryUsageMb DESC")
    List<EventProcessingHistory> findByEventoIdOrderByMemoryUsageDesc(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId ORDER BY eph.cpuUsagePercent DESC")
    List<EventProcessingHistory> findByEventoIdOrderByCpuUsageDesc(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId ORDER BY eph.inputSizeBytes DESC")
    List<EventProcessingHistory> findByEventoIdOrderByInputSizeDesc(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId ORDER BY eph.outputSizeBytes DESC")
    List<EventProcessingHistory> findByEventoIdOrderByOutputSizeDesc(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId ORDER BY eph.dataCriacao DESC")
    List<EventProcessingHistory> findByEventoIdOrderByCreationDateDesc(@Param("eventoId") Long eventoId);
    
    @Query("SELECT eph FROM EventProcessingHistory eph WHERE eph.evento.id = :eventoId ORDER BY eph.dataCriacao ASC")
    List<EventProcessingHistory> findByEventoIdOrderByCreationDateAsc(@Param("eventoId") Long eventoId);
}
