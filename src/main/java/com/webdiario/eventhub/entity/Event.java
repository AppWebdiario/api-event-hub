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
@Table(name = "eventos")
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
    @Column(name = "tipo_evento", nullable = false, length = 100)
    private String tipoEvento;

    @NotBlank(message = "Event source is required")
    @Column(name = "origem", nullable = false, length = 100)
    private String origem;

    @NotBlank(message = "Event version is required")
    @Column(name = "versao", nullable = false, length = 20)
    private String versao;

    @NotNull(message = "Event timestamp is required")
    @Column(name = "timestamp_evento", nullable = false)
    private LocalDateTime timestampEvento;

    @Column(name = "timestamp_processamento", nullable = false)
    @Builder.Default
    private LocalDateTime timestampProcessamento = LocalDateTime.now();

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
    @Column(name = "prioridade", length = 20)
    @Builder.Default
    private EventPriority prioridade = EventPriority.NORMAL;

    @Column(name = "tentativas_processamento")
    @Builder.Default
    private Integer tentativasProcessamento = 0;

    @Column(name = "max_tentativas")
    @Builder.Default
    private Integer maxTentativas = 3;

    @Column(name = "proxima_tentativa")
    private LocalDateTime proximaTentativa;

    @Column(name = "erro_processamento", columnDefinition = "TEXT")
    private String erroProcessamento;

    @Column(name = "metadata", columnDefinition = "JSON")
    private String metadata;

    @Column(name = "tags", length = 500)
    private String tags;

    @Column(name = "duracao_processamento")
    private Long duracaoProcessamento; // milliseconds

    @Column(name = "tamanho_payload")
    private Long tamanhoPayload; // bytes

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
    @Column(name = "data_criacao", nullable = false)
    @Builder.Default
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "usuario_criacao", length = 100)
    private String usuarioCriacao;

    @Column(name = "usuario_atualizacao", length = 100)
    private String usuarioAtualizacao;

    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    // Helper methods
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean canRetry() {
        return tentativasProcessamento < maxTentativas;
    }

    public boolean isRetryable() {
        return status == EventStatus.FAILED && canRetry();
    }

    public void incrementTentativas() {
        this.tentativasProcessamento++;
    }

    public void setNextRetry(LocalDateTime nextRetry) {
        this.proximaTentativa = nextRetry;
    }

    public enum EventStatus {
        PENDING, PROCESSING, PROCESSED, FAILED, RETRY, CANCELLED, EXPIRED
    }

    public enum EventPriority {
        LOW, NORMAL, HIGH, URGENT, CRITICAL
    }
}
