package com.webdiario.eventhub.dto;

import java.time.LocalDateTime;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating event schemas
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSchemaCreateRequest {

    @NotBlank(message = "Event type is required")
    @Size(max = 100, message = "Event type must not exceed 100 characters")
    private String tipoEvento;

    @NotBlank(message = "Event version is required")
    @Size(max = 20, message = "Event version must not exceed 20 characters")
    private String versao;

    @NotBlank(message = "Schema JSON is required")
    private String schemaJson;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String descricao;

    private Boolean active;

    private Boolean deprecated;

    private LocalDateTime deprecationDate;

    @Size(max = 2000, message = "Migration guide must not exceed 2000 characters")
    private String migrationGuide;

    private String examples;

    private Map<String, Object> metadata;

    @Size(max = 500, message = "Tags must not exceed 500 characters")
    private String tags;

    private String validationRules;

    @Size(max = 1000, message = "Required fields must not exceed 1000 characters")
    private String requiredFields;

    @Size(max = 1000, message = "Optional fields must not exceed 1000 characters")
    private String optionalFields;

    private String fieldDescriptions;

    @Size(max = 2000, message = "Business rules must not exceed 2000 characters")
    private String businessRules;

    @Size(max = 2000, message = "Data quality rules must not exceed 2000 characters")
    private String dataQualityRules;

    private String compatibilityMatrix;

    @Size(max = 100, message = "Created by must not exceed 100 characters")
    private String createdBy;
}
