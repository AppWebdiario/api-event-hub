package com.webdiario.eventhub.repository;

import com.webdiario.eventhub.entity.EventSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for EventSchema entity
 */
@Repository
public interface EventSchemaRepository extends JpaRepository<EventSchema, Long> {

    Optional<EventSchema> findByEventTypeAndVersion(String eventType, String version);
    
    List<EventSchema> findByEventType(String eventType);
    
    List<EventSchema> findByVersion(String version);
    
    List<EventSchema> findByActiveTrue();
    
    List<EventSchema> findByDeprecatedTrue();
    
    List<EventSchema> findByActiveTrueAndDeprecatedFalse();
    
    @Query("SELECT es FROM EventSchema es WHERE es.eventType = :eventType AND es.active = true ORDER BY es.version DESC")
    List<EventSchema> findActiveSchemasByEventType(@Param("eventType") String eventType);
    
    @Query("SELECT es FROM EventSchema es WHERE es.eventType = :eventType ORDER BY es.version DESC")
    List<EventSchema> findAllSchemasByEventType(@Param("eventType") String eventType);
    
    @Query("SELECT es FROM EventSchema es WHERE es.tags LIKE %:tag%")
    List<EventSchema> findByTag(@Param("tag") String tag);
    
    @Query("SELECT es FROM EventSchema es WHERE es.createdBy = :createdBy")
    List<EventSchema> findByCreatedBy(@Param("createdBy") String createdBy);
    
    @Query("SELECT es FROM EventSchema es WHERE es.updatedBy = :updatedBy")
    List<EventSchema> findByUpdatedBy(@Param("updatedBy") String updatedBy);
    
    @Query("SELECT es.eventType, COUNT(es) FROM EventSchema es GROUP BY es.eventType")
    List<Object[]> countByEventType();
    
    @Query("SELECT es.version, COUNT(es) FROM EventSchema es GROUP BY es.version")
    List<Object[]> countByVersion();
    
    @Query("SELECT es FROM EventSchema es WHERE es.usageCount > :minUsage")
    List<EventSchema> findFrequentlyUsedSchemas(@Param("minUsage") Long minUsage);
    
    @Query("SELECT es FROM EventSchema es WHERE es.lastUsed IS NOT NULL ORDER BY es.lastUsed DESC")
    List<EventSchema> findRecentlyUsedSchemas();
    
    @Query("SELECT es FROM EventSchema es WHERE es.deprecationDate IS NOT NULL AND es.deprecationDate <= CURRENT_TIMESTAMP")
    List<EventSchema> findDeprecatedSchemas();
    
    @Query("SELECT es FROM EventSchema es WHERE es.deprecationDate IS NOT NULL AND es.deprecationDate > CURRENT_TIMESTAMP")
    List<EventSchema> findSchemasToBeDeprecated();
    
    @Query("SELECT es FROM EventSchema es WHERE es.schemaJson LIKE %:fieldName%")
    List<EventSchema> findSchemasContainingField(@Param("fieldName") String fieldName);
    
    @Query("SELECT es FROM EventSchema es WHERE es.requiredFields LIKE %:requiredField%")
    List<EventSchema> findSchemasWithRequiredField(@Param("requiredField") String requiredField);
    
    @Query("SELECT es FROM EventSchema es WHERE es.optionalFields LIKE %:optionalField%")
    List<EventSchema> findSchemasWithOptionalField(@Param("optionalField") String optionalField);
    
    @Query("SELECT es FROM EventSchema es WHERE es.businessRules LIKE %:rule%")
    List<EventSchema> findSchemasWithBusinessRule(@Param("rule") String rule);
    
    @Query("SELECT es FROM EventSchema es WHERE es.dataQualityRules LIKE %:rule%")
    List<EventSchema> findSchemasWithDataQualityRule(@Param("rule") String rule);
    
    @Query("SELECT es FROM EventSchema es WHERE es.compatibilityMatrix LIKE %:compatibility%")
    List<EventSchema> findSchemasWithCompatibility(@Param("compatibility") String compatibility);
    
    @Query("SELECT es FROM EventSchema es WHERE es.examples IS NOT NULL AND es.examples != ''")
    List<EventSchema> findSchemasWithExamples();
    
    @Query("SELECT es FROM EventSchema es WHERE es.migrationGuide IS NOT NULL AND es.migrationGuide != ''")
    List<EventSchema> findSchemasWithMigrationGuide();
    
    @Query("SELECT es FROM EventSchema es WHERE es.validationRules IS NOT NULL AND es.validationRules != ''")
    List<EventSchema> findSchemasWithValidationRules();
    
    @Query("SELECT es FROM EventSchema es WHERE es.fieldDescriptions IS NOT NULL AND es.fieldDescriptions != ''")
    List<EventSchema> findSchemasWithFieldDescriptions();
    
    @Query("SELECT es FROM EventSchema es WHERE es.metadata LIKE %:metadataKey%")
    List<EventSchema> findSchemasByMetadataKey(@Param("metadataKey") String metadataKey);
    
    @Query("SELECT es FROM EventSchema es WHERE es.description LIKE %:description%")
    List<EventSchema> findByDescriptionContaining(@Param("description") String description);
}
