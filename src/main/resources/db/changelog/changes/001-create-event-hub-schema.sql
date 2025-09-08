-- Create event hub database schema
-- This script creates all necessary tables for the event hub system

-- Events table
CREATE TABLE IF NOT EXISTS eventos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id VARCHAR(100) NOT NULL UNIQUE,
    tipo_evento VARCHAR(100) NOT NULL,
    origem VARCHAR(100) NOT NULL,
    versao VARCHAR(20) NOT NULL,
    timestamp_evento DATETIME NOT NULL,
    timestamp_processamento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    correlation_id VARCHAR(100),
    user_id VARCHAR(100),
    tenant_id VARCHAR(100),
    payload JSON,
    schema_version VARCHAR(20),
    status ENUM('PENDING', 'PROCESSING', 'PROCESSED', 'FAILED', 'RETRY', 'CANCELLED', 'EXPIRED') NOT NULL DEFAULT 'PROCESSED',
    prioridade ENUM('LOW', 'NORMAL', 'HIGH', 'URGENT', 'CRITICAL') NOT NULL DEFAULT 'NORMAL',
    tentativas_processamento INT NOT NULL DEFAULT 0,
    max_tentativas INT NOT NULL DEFAULT 3,
    proxima_tentativa DATETIME,
    erro_processamento TEXT,
    metadata JSON,
    tags VARCHAR(500),
    duracao_processamento BIGINT,
    tamanho_payload BIGINT,
    hash_payload VARCHAR(64),
    compressed BOOLEAN NOT NULL DEFAULT FALSE,
    encrypted BOOLEAN NOT NULL DEFAULT FALSE,
    partition_key VARCHAR(100),
    shard_id VARCHAR(50),
    sequence_number BIGINT,
    expires_at DATETIME,
    retention_days INT NOT NULL DEFAULT 90,
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao DATETIME ON UPDATE CURRENT_TIMESTAMP,
    usuario_criacao VARCHAR(100),
    usuario_atualizacao VARCHAR(100),
    
    INDEX idx_event_id (event_id),
    INDEX idx_tipo_evento (tipo_evento),
    INDEX idx_origem (origem),
    INDEX idx_versao (versao),
    INDEX idx_timestamp_evento (timestamp_evento),
    INDEX idx_timestamp_processamento (timestamp_processamento),
    INDEX idx_correlation_id (correlation_id),
    INDEX idx_user_id (user_id),
    INDEX idx_tenant_id (tenant_id),
    INDEX idx_status (status),
    INDEX idx_prioridade (prioridade),
    INDEX idx_tentativas_processamento (tentativas_processamento),
    INDEX idx_proxima_tentativa (proxima_tentativa),
    INDEX idx_hash_payload (hash_payload),
    INDEX idx_partition_key (partition_key),
    INDEX idx_shard_id (shard_id),
    INDEX idx_sequence_number (sequence_number),
    INDEX idx_expires_at (expires_at),
    INDEX idx_data_criacao (data_criacao),
    INDEX idx_compressed (compressed),
    INDEX idx_encrypted (encrypted),
    INDEX idx_tags (tags)
);

-- Event schemas table
CREATE TABLE IF NOT EXISTS event_schemas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_evento VARCHAR(100) NOT NULL,
    versao VARCHAR(20) NOT NULL,
    schema_json JSON NOT NULL,
    descricao VARCHAR(500),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
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
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao DATETIME ON UPDATE CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_tipo_evento_versao (tipo_evento, versao),
    INDEX idx_tipo_evento (tipo_evento),
    INDEX idx_versao (versao),
    INDEX idx_ativo (ativo),
    INDEX idx_deprecated (deprecated),
    INDEX idx_deprecation_date (deprecation_date),
    INDEX idx_usage_count (usage_count),
    INDEX idx_last_used (last_used),
    INDEX idx_created_by (created_by),
    INDEX idx_updated_by (updated_by),
    INDEX idx_data_criacao (data_criacao),
    INDEX idx_tags (tags)
);

-- Event processing history table
CREATE TABLE IF NOT EXISTS event_processing_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    evento_id BIGINT NOT NULL,
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
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (evento_id) REFERENCES eventos(id) ON DELETE CASCADE,
    
    INDEX idx_evento_id (evento_id),
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
    INDEX idx_data_criacao (data_criacao)
);

-- Create indexes for better performance
CREATE INDEX idx_eventos_composite_1 ON eventos (tipo_evento, status, timestamp_evento);
CREATE INDEX idx_eventos_composite_2 ON eventos (origem, status, timestamp_evento);
CREATE INDEX idx_eventos_composite_3 ON eventos (user_id, tenant_id, timestamp_evento);
CREATE INDEX idx_eventos_composite_4 ON eventos (correlation_id, timestamp_evento);
CREATE INDEX idx_eventos_composite_5 ON eventos (status, proxima_tentativa);

CREATE INDEX idx_event_schemas_composite_1 ON event_schemas (tipo_evento, ativo, deprecated);
CREATE INDEX idx_event_schemas_composite_2 ON event_schemas (tipo_evento, versao, ativo);

CREATE INDEX idx_event_processing_history_composite_1 ON event_processing_history (evento_id, attempt_number, status);
CREATE INDEX idx_event_processing_history_composite_2 ON event_processing_history (processor_id, status, processing_start);
CREATE INDEX idx_event_processing_history_composite_3 ON event_processing_history (status, processing_start, processing_end);
CREATE INDEX idx_event_processing_history_composite_4 ON event_processing_history (error_code, status, processing_start);
