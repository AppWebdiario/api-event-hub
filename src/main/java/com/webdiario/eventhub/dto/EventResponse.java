package com.webdiario.eventhub.dto;

import com.webdiario.eventhub.entity.Evento;
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
    private String tipoEvento;
    private String origem;
    private String versao;
    private LocalDateTime timestampEvento;
    private LocalDateTime timestampProcessamento;
    private String correlationId;
    private String userId;
    private String tenantId;
    private String payload;
    private String schemaVersion;
    private Evento.EventStatus status;
    private Evento.EventPriority prioridade;
    private Integer tentativasProcessamento;
    private String erroProcessamento;
    private String metadata;
    private String tags;
    private Long duracaoProcessamento;
    private Long tamanhoPayload;
    private String hashPayload;
    private Boolean compressed;
    private Boolean encrypted;
    private String partitionKey;
    private String shardId;
    private Long sequenceNumber;
    private LocalDateTime expiresAt;
    private Integer retentionDays;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String usuarioCriacao;
    private String usuarioAtualizacao;

    public static EventResponse fromEntity(Evento evento) {
        return EventResponse.builder()
                .id(evento.getId())
                .eventId(evento.getEventId())
                .tipoEvento(evento.getTipoEvento())
                .origem(evento.getOrigem())
                .versao(evento.getVersao())
                .timestampEvento(evento.getTimestampEvento())
                .timestampProcessamento(evento.getTimestampProcessamento())
                .correlationId(evento.getCorrelationId())
                .userId(evento.getUserId())
                .tenantId(evento.getTenantId())
                .payload(evento.getPayload())
                .schemaVersion(evento.getSchemaVersion())
                .status(evento.getStatus())
                .prioridade(evento.getPrioridade())
                .tentativasProcessamento(evento.getTentativasProcessamento())
                .erroProcessamento(evento.getErroProcessamento())
                .metadata(evento.getMetadata())
                .tags(evento.getTags())
                .duracaoProcessamento(evento.getDuracaoProcessamento())
                .tamanhoPayload(evento.getTamanhoPayload())
                .hashPayload(evento.getHashPayload())
                .compressed(evento.getCompressed())
                .encrypted(evento.getEncrypted())
                .partitionKey(evento.getPartitionKey())
                .shardId(evento.getShardId())
                .sequenceNumber(evento.getSequenceNumber())
                .expiresAt(evento.getExpiresAt())
                .retentionDays(evento.getRetentionDays())
                .dataCriacao(evento.getDataCriacao())
                .dataAtualizacao(evento.getDataAtualizacao())
                .usuarioCriacao(evento.getUsuarioCriacao())
                .usuarioAtualizacao(evento.getUsuarioAtualizacao())
                .build();
    }
}
