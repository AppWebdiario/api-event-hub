package com.webdiario.eventhub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity for storing JSON schemas for event validation
 */
@Entity
@Table(name = "event_schemas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Event type is required")
    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @NotBlank(message = "Event version is required")
    @Column(name = "version", nullable = false, length = 20)
    private String version;

    @NotBlank(message = "Schema content is required")
    @Column(name = "schema_json", columnDefinition = "JSON", nullable = false)
    private String schemaJson;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "deprecated", nullable = false)
    @Builder.Default
    private Boolean deprecated = false;

    @Column(name = "deprecation_date")
    private LocalDateTime deprecationDate;

    @Column(name = "migration_guide", columnDefinition = "TEXT")
    private String migrationGuide;

    @Column(name = "examples", columnDefinition = "JSON")
    private String examples;

    @Column(name = "metadata", columnDefinition = "JSON")
    private String metadata;

    @Column(name = "tags", length = 500)
    private String tags;

    @Column(name = "validation_rules", columnDefinition = "JSON")
    private String validationRules;

    @Column(name = "required_fields", length = 1000)
    private String requiredFields;

    @Column(name = "optional_fields", length = 1000)
    private String optionalFields;

    @Column(name = "field_descriptions", columnDefinition = "JSON")
    private String fieldDescriptions;

    @Column(name = "business_rules", columnDefinition = "TEXT")
    private String businessRules;

    @Column(name = "data_quality_rules", columnDefinition = "TEXT")
    private String dataQualityRules;

    @Column(name = "compatibility_matrix", columnDefinition = "JSON")
    private String compatibilityMatrix;

    @Column(name = "usage_count")
    @Builder.Default
    private Long usageCount = 0L;

    @Column(name = "last_used")
    private LocalDateTime lastUsed;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Helper methods
    public boolean isActive() {
        return active && !deprecated;
    }

    public void incrementUsage() {
        this.usageCount++;
        this.lastUsed = LocalDateTime.now();
    }

    public String getFullVersion() {
        return eventType + "-" + version;
    }
}
