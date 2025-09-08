package com.webdiario.eventhub.repository;

import com.webdiario.eventhub.entity.Evento;
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
 * Repository for Evento entity
 */
@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    Optional<Evento> findByEventId(String eventId);
    
    List<Evento> findByTipoEvento(String tipoEvento);
    
    List<Evento> findByOrigem(String origem);
    
    List<Evento> findByStatus(Evento.EventStatus status);
    
    List<Evento> findByPrioridade(Evento.EventPriority prioridade);
    
    List<Evento> findByUserId(String userId);
    
    List<Evento> findByTenantId(String tenantId);
    
    List<Evento> findByCorrelationId(String correlationId);
    
    List<Evento> findByTimestampEventoBetween(LocalDateTime start, LocalDateTime end);
    
    List<Evento> findByTimestampProcessamentoBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT e FROM Evento e WHERE e.timestampEvento < :date AND e.status = 'PROCESSED'")
    List<Evento> findExpiredEvents(@Param("date") LocalDateTime date);
    
    @Query("SELECT e FROM Evento e WHERE e.tentativasProcessamento >= e.maxTentativas AND e.status = 'FAILED'")
    List<Evento> findFailedEventsExceedingMaxRetries();
    
    @Query("SELECT e FROM Evento e WHERE e.proximaTentativa <= :now AND e.status = 'RETRY'")
    List<Evento> findRetryableEvents(@Param("now") LocalDateTime now);
    
    @Query("SELECT e FROM Evento e WHERE e.tipoEvento = :tipoEvento AND e.versao = :versao")
    List<Evento> findByTipoEventoAndVersao(@Param("tipoEvento") String tipoEvento, @Param("versao") String versao);
    
    @Query("SELECT e FROM Evento e WHERE e.payload LIKE %:searchTerm% OR e.metadata LIKE %:searchTerm%")
    Page<Evento> findByPayloadOrMetadataContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT e FROM Evento e WHERE e.tags LIKE %:tag%")
    List<Evento> findByTag(@Param("tag") String tag);
    
    @Query("SELECT e FROM Evento e WHERE e.hashPayload = :hash")
    List<Evento> findByHashPayload(@Param("hash") String hash);
    
    @Query("SELECT e FROM Evento e WHERE e.partitionKey = :partitionKey")
    List<Evento> findByPartitionKey(@Param("partitionKey") String partitionKey);
    
    @Query("SELECT e FROM Evento e WHERE e.shardId = :shardId")
    List<Evento> findByShardId(@Param("shardId") String shardId);
    
    @Query("SELECT COUNT(e) FROM Evento e WHERE e.tipoEvento = :tipoEvento AND e.status = :status")
    Long countByTipoEventoAndStatus(@Param("tipoEvento") String tipoEvento, @Param("status") Evento.EventStatus status);
    
    @Query("SELECT e.tipoEvento, COUNT(e) FROM Evento e GROUP BY e.tipoEvento")
    List<Object[]> countByTipoEvento();
    
    @Query("SELECT e.status, COUNT(e) FROM Evento e GROUP BY e.status")
    List<Object[]> countByStatus();
    
    @Query("SELECT e.prioridade, COUNT(e) FROM Evento e GROUP BY e.prioridade")
    List<Object[]> countByPrioridade();
    
    @Query("SELECT e.origem, COUNT(e) FROM Evento e GROUP BY e.origem")
    List<Object[]> countByOrigem();
    
    @Query("SELECT AVG(e.duracaoProcessamento) FROM Evento e WHERE e.duracaoProcessamento IS NOT NULL")
    Double getAverageProcessingTime();
    
    @Query("SELECT MAX(e.duracaoProcessamento) FROM Evento e WHERE e.duracaoProcessamento IS NOT NULL")
    Long getMaxProcessingTime();
    
    @Query("SELECT MIN(e.duracaoProcessamento) FROM Evento e WHERE e.duracaoProcessamento IS NOT NULL")
    Long getMinProcessingTime();
    
    @Query("SELECT e FROM Evento e WHERE e.timestampEvento >= :startDate ORDER BY e.timestampEvento DESC")
    Page<Evento> findRecentEvents(@Param("startDate") LocalDateTime startDate, Pageable pageable);
    
    @Query("SELECT e FROM Evento e WHERE e.timestampEvento >= :startDate AND e.timestampEvento <= :endDate")
    List<Evento> findEventsInTimeRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT e FROM Evento e WHERE e.compressed = true")
    List<Evento> findCompressedEvents();
    
    @Query("SELECT e FROM Evento e WHERE e.encrypted = true")
    List<Evento> findEncryptedEvents();
    
    @Query("SELECT e FROM Evento e WHERE e.tamanhoPayload > :minSize")
    List<Evento> findLargeEvents(@Param("minSize") Long minSize);
    
    @Query("SELECT e FROM Evento e WHERE e.erroProcessamento IS NOT NULL AND e.erroProcessamento != ''")
    List<Evento> findEventsWithErrors();
    
    @Query("SELECT e FROM Evento e WHERE e.erroProcessamento LIKE %:errorPattern%")
    List<Evento> findEventsByErrorPattern(@Param("errorPattern") String errorPattern);
}
