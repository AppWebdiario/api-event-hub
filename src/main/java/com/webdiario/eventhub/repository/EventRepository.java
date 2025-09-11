package com.webdiario.eventhub.repository;

import com.webdiario.eventhub.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Event entity
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByEventId(String eventId);
    
    List<Event> findByEventType(String eventType);
    
    List<Event> findBySource(String source);
    
    List<Event> findByStatus(Event.EventStatus status);
    
    List<Event> findByPriority(Event.EventPriority priority);
    
    List<Event> findByUserId(String userId);
    
    List<Event> findByTenantId(String tenantId);
    
    List<Event> findByCorrelationId(String correlationId);
    
    List<Event> findByEventTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    List<Event> findByProcessingTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT e FROM Event e WHERE e.eventTimestamp < :date AND e.status = 'PROCESSED'")
    List<Event> findExpiredEvents(@Param("date") LocalDateTime date);
    
    @Query("SELECT e FROM Event e WHERE e.processingAttempts >= e.maxAttempts AND e.status = 'FAILED'")
    List<Event> findFailedEventsExceedingMaxRetries();
    
    @Query("SELECT e FROM Event e WHERE e.nextRetry <= :now AND e.status = 'RETRY'")
    List<Event> findRetryableEvents(@Param("now") LocalDateTime now);
    
    @Query("SELECT e FROM Event e WHERE e.eventType = :eventType AND e.version = :version")
    List<Event> findByEventTypeAndVersion(@Param("eventType") String eventType, @Param("version") String version);
    
    @Query("SELECT e FROM Event e WHERE e.payload LIKE %:searchTerm% OR e.metadata LIKE %:searchTerm%")
    Page<Event> findByPayloadOrMetadataContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT e FROM Event e WHERE e.tags LIKE %:tag%")
    List<Event> findByTag(@Param("tag") String tag);
    
    @Query("SELECT e FROM Event e WHERE e.hashPayload = :hash")
    List<Event> findByHashPayload(@Param("hash") String hash);
    
    @Query("SELECT e FROM Event e WHERE e.partitionKey = :partitionKey")
    List<Event> findByPartitionKey(@Param("partitionKey") String partitionKey);
    
    @Query("SELECT e FROM Event e WHERE e.shardId = :shardId")
    List<Event> findByShardId(@Param("shardId") String shardId);
    
    @Query("SELECT COUNT(e) FROM Event e WHERE e.eventType = :eventType AND e.status = :status")
    Long countByEventTypeAndStatus(@Param("eventType") String eventType, @Param("status") Event.EventStatus status);
    
    @Query("SELECT e.eventType, COUNT(e) FROM Event e GROUP BY e.eventType")
    List<Object[]> countByEventType();
    
    @Query("SELECT e.status, COUNT(e) FROM Event e GROUP BY e.status")
    List<Object[]> countByStatus();
    
    @Query("SELECT e.priority, COUNT(e) FROM Event e GROUP BY e.priority")
    List<Object[]> countByPriority();
    
    @Query("SELECT e.source, COUNT(e) FROM Event e GROUP BY e.source")
    List<Object[]> countBySource();
    
    @Query("SELECT AVG(e.processingDuration) FROM Event e WHERE e.processingDuration IS NOT NULL")
    Double getAverageProcessingTime();
    
    @Query("SELECT MAX(e.processingDuration) FROM Event e WHERE e.processingDuration IS NOT NULL")
    Long getMaxProcessingTime();
    
    @Query("SELECT MIN(e.processingDuration) FROM Event e WHERE e.processingDuration IS NOT NULL")
    Long getMinProcessingTime();
    
    @Query("SELECT e FROM Event e WHERE e.eventTimestamp >= :startDate ORDER BY e.eventTimestamp DESC")
    Page<Event> findRecentEvents(@Param("startDate") LocalDateTime startDate, Pageable pageable);
    
    @Query("SELECT e FROM Event e WHERE e.eventTimestamp >= :startDate AND e.eventTimestamp <= :endDate")
    List<Event> findEventsInTimeRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT e FROM Event e WHERE e.compressed = true")
    List<Event> findCompressedEvents();
    
    @Query("SELECT e FROM Event e WHERE e.encrypted = true")
    List<Event> findEncryptedEvents();
    
    @Query("SELECT e FROM Event e WHERE e.payloadSize > :minSize")
    List<Event> findLargeEvents(@Param("minSize") Long minSize);
    
    @Query("SELECT e FROM Event e WHERE e.processingError IS NOT NULL AND e.processingError != ''")
    List<Event> findEventsWithErrors();
    
    @Query("SELECT e FROM Event e WHERE e.processingError LIKE %:errorPattern%")
    List<Event> findEventsByErrorPattern(@Param("errorPattern") String errorPattern);
}
