-- Create event hub database schema
-- This script creates all necessary tables for the event hub system

-- Events table
CREATE TABLE IF NOT EXISTS events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id VARCHAR(100) NOT NULL UNIQUE,
    event_type VARCHAR(100) NOT NULL,
    source VARCHAR(100) NOT NULL,
    version VARCHAR(20) NOT NULL,
    event_timestamp DATETIME NOT NULL,
    processing_timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    correlation_id VARCHAR(100),
    user_id VARCHAR(100),
    tenant_id VARCHAR(100),
    payload JSON,
    schema_version VARCHAR(20),
    status ENUM('PENDING', 'PROCESSING', 'PROCESSED', 'FAILED', 'RETRY', 'CANCELLED', 'EXPIRED') NOT NULL DEFAULT 'PROCESSED',
    priority ENUM('LOW', 'NORMAL', 'HIGH', 'URGENT', 'CRITICAL') NOT NULL DEFAULT 'NORMAL',
    processing_attempts INT NOT NULL DEFAULT 0,
    max_attempts INT NOT NULL DEFAULT 3,
    next_retry DATETIME,
    processing_error TEXT,
    metadata JSON,
    tags VARCHAR(500),
    processing_duration BIGINT,
    payload_size BIGINT,
    hash_payload VARCHAR(64),
    compressed BOOLEAN NOT NULL DEFAULT FALSE,
    encrypted BOOLEAN NOT NULL DEFAULT FALSE,
    partition_key VARCHAR(100),
    shard_id VARCHAR(50),
    sequence_number BIGINT,
    expires_at DATETIME,
    retention_days INT NOT NULL DEFAULT 90,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    
    INDEX idx_event_id (event_id),
    INDEX idx_event_type (event_type),
    INDEX idx_source (source),
    INDEX idx_version (version),
    INDEX idx_event_timestamp (event_timestamp),
    INDEX idx_processing_timestamp (processing_timestamp),
    INDEX idx_correlation_id (correlation_id),
    INDEX idx_user_id (user_id),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_status (status),
    INDEX idx_priority (priority),
    INDEX idx_processing_attempts (processing_attempts),
    INDEX idx_next_retry (next_retry),
    INDEX idx_hash_payload (hash_payload),
    INDEX idx_partition_key (partition_key),
    INDEX idx_shard_id (shard_id),
    INDEX idx_sequence_number (sequence_number),
    INDEX idx_expires_at (expires_at),
    INDEX idx_created_at (created_at),
    INDEX idx_compressed (compressed),
    INDEX idx_encrypted (encrypted),
    INDEX idx_tags (tags)
);

-- Event schemas table
CREATE TABLE IF NOT EXISTS event_schemas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    version VARCHAR(20) NOT NULL,
    schema_json JSON NOT NULL,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    deprecated BOOLEAN NOT NULL DEFAULT FALSE,
    deprecation_date DATETIME,
    migration_guide TEXT,
    examples JSON,
    metadata JSON,
    tags VARCHAR(500),
    validation_rules JSON,
    required_fields VARCHAR(1000),
    optional_fields VARCHAR(1000),
    field_descriptions JSON,
    business_rules TEXT,
    data_quality_rules TEXT,
    compatibility_matrix JSON,
    usage_count BIGINT NOT NULL DEFAULT 0,
    last_used DATETIME,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_event_type_version (event_type, version),
    INDEX idx_event_type (event_type),
    INDEX idx_version (version),
    INDEX idx_active (active),
    INDEX idx_deprecated (deprecated),
    INDEX idx_deprecation_date (deprecation_date),
    INDEX idx_usage_count (usage_count),
    INDEX idx_last_used (last_used),
    INDEX idx_created_by (created_by),
    INDEX idx_updated_by (updated_by),
    INDEX idx_created_at (created_at),
    INDEX idx_tags (tags)
);

-- Event processing history table
CREATE TABLE IF NOT EXISTS event_processing_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    attempt_number INT NOT NULL,
    processor_id VARCHAR(100),
    processor_type VARCHAR(50),
    processing_start DATETIME NOT NULL,
    processing_end DATETIME,
    duration_ms BIGINT,
    status ENUM('STARTED', 'PROCESSING', 'SUCCESS', 'FAILED', 'CANCELLED', 'TIMEOUT', 'RETRY') NOT NULL,
    error_message TEXT,
    error_stack_trace TEXT,
    error_code VARCHAR(50),
    retry_count INT,
    next_retry DATETIME,
    processing_metadata JSON,
    input_payload_hash VARCHAR(64),
    output_payload_hash VARCHAR(64),
    input_size_bytes BIGINT,
    output_size_bytes BIGINT,
    memory_usage_mb BIGINT,
    cpu_usage_percent DOUBLE,
    thread_id VARCHAR(50),
    host_name VARCHAR(100),
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    correlation_id VARCHAR(100),
    trace_id VARCHAR(100),
    span_id VARCHAR(100),
    tags VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    
    INDEX idx_event_id (event_id),
    INDEX idx_attempt_number (attempt_number),
    INDEX idx_processor_id (processor_id),
    INDEX idx_processor_type (processor_type),
    INDEX idx_processing_start (processing_start),
    INDEX idx_processing_end (processing_end),
    INDEX idx_duration_ms (duration_ms),
    INDEX idx_status (status),
    INDEX idx_error_code (error_code),
    INDEX idx_retry_count (retry_count),
    INDEX idx_next_retry (next_retry),
    INDEX idx_input_payload_hash (input_payload_hash),
    INDEX idx_output_payload_hash (output_payload_hash),
    INDEX idx_input_size_bytes (input_size_bytes),
    INDEX idx_output_size_bytes (output_size_bytes),
    INDEX idx_memory_usage_mb (memory_usage_mb),
    INDEX idx_cpu_usage_percent (cpu_usage_percent),
    INDEX idx_thread_id (thread_id),
    INDEX idx_host_name (host_name),
    INDEX idx_ip_address (ip_address),
    INDEX idx_correlation_id (correlation_id),
    INDEX idx_trace_id (trace_id),
    INDEX idx_span_id (span_id),
    INDEX idx_tags (tags),
    INDEX idx_created_at (created_at)
);

-- Create composite indexes for better performance
CREATE INDEX idx_events_composite_1 ON events (event_type, status, event_timestamp);
CREATE INDEX idx_events_composite_2 ON events (source, status, event_timestamp);
CREATE INDEX idx_events_composite_3 ON events (user_id, tenant_id, event_timestamp);
CREATE INDEX idx_events_composite_4 ON events (correlation_id, event_timestamp);
CREATE INDEX idx_events_composite_5 ON events (status, next_retry);

CREATE INDEX idx_event_schemas_composite_1 ON event_schemas (event_type, active, deprecated);
CREATE INDEX idx_event_schemas_composite_2 ON event_schemas (event_type, version, active);

CREATE INDEX idx_event_processing_history_composite_1 ON event_processing_history (event_id, attempt_number, status);
CREATE INDEX idx_event_processing_history_composite_2 ON event_processing_history (processor_id, status, processing_start);
CREATE INDEX idx_event_processing_history_composite_3 ON event_processing_history (status, processing_start, processing_end);
CREATE INDEX idx_event_processing_history_composite_4 ON event_processing_history (error_code, status, processing_start);
