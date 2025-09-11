-- Insert initial event schemas
-- This script creates default JSON schemas for common event types

-- User Created Event Schema
INSERT INTO event_schemas (event_type, version, schema_json, description, active, tags, required_fields, optional_fields, created_by) VALUES
('USER_CREATED', '1.0', 
'{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "type": "object",
  "title": "User Created Event",
  "description": "Event emitted when a new user is created in the system",
  "properties": {
    "userId": {
      "type": "string",
      "description": "Unique identifier for the user",
      "pattern": "^[a-zA-Z0-9_-]+$",
      "minLength": 3,
      "maxLength": 50
    },
    "username": {
      "type": "string",
      "description": "Username for the user account",
      "minLength": 3,
      "maxLength": 50
    },
    "email": {
      "type": "string",
      "description": "Email address of the user",
      "format": "email",
      "maxLength": 255
    },
    "fullName": {
      "type": "string",
      "description": "Full name of the user",
      "minLength": 2,
      "maxLength": 100
    },
    "roles": {
      "type": "array",
      "description": "List of roles assigned to the user",
      "items": {
        "type": "string"
      },
      "minItems": 1
    },
    "tenantId": {
      "type": "string",
      "description": "Tenant identifier for multi-tenancy",
      "maxLength": 100
    },
    "metadata": {
      "type": "object",
      "description": "Additional metadata about the user creation",
      "additionalProperties": true
    }
  },
  "required": ["userId", "username", "email", "fullName", "roles"],
  "additionalProperties": false
}',
'Event emitted when a new user is created in the system', 
TRUE, 
'user,creation,account', 
'userId,username,email,fullName,roles', 
'tenantId,metadata', 
'system');

-- User Updated Event Schema
INSERT INTO event_schemas (event_type, version, schema_json, description, active, tags, required_fields, optional_fields, created_by) VALUES
('USER_UPDATED', '1.0', 
'{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "type": "object",
  "title": "User Updated Event",
  "description": "Event emitted when a user profile is updated",
  "properties": {
    "userId": {
      "type": "string",
      "description": "Unique identifier for the user",
      "pattern": "^[a-zA-Z0-9_-]+$"
    },
    "updatedFields": {
      "type": "array",
      "description": "List of fields that were updated",
      "items": {
        "type": "string",
        "enum": ["username", "email", "fullName", "roles", "status", "password"]
      }
    },
    "previousValues": {
      "type": "object",
      "description": "Previous values of updated fields",
      "additionalProperties": true
    },
    "newValues": {
      "type": "object",
      "description": "New values of updated fields",
      "additionalProperties": true
    },
    "updatedBy": {
      "type": "string",
      "description": "User ID who performed the update",
      "maxLength": 100
    },
    "reason": {
      "type": "string",
      "description": "Reason for the update",
      "maxLength": 500
    }
  },
  "required": ["userId", "updatedFields"],
  "additionalProperties": false
}',
'Event emitted when a user profile is updated', 
TRUE, 
'user,update,profile', 
'userId,updatedFields', 
'previousValues,newValues,updatedBy,reason', 
'system');

-- User Login Event Schema
INSERT INTO event_schemas (event_type, version, schema_json, description, active, tags, required_fields, optional_fields, created_by) VALUES
('USER_LOGIN', '1.0', 
'{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "type": "object",
  "title": "User Login Event",
  "description": "Event emitted when a user successfully logs in",
  "properties": {
    "userId": {
      "type": "string",
      "description": "Unique identifier for the user"
    },
    "username": {
      "type": "string",
      "description": "Username used for login"
    },
    "loginTime": {
      "type": "string",
      "description": "ISO 8601 timestamp of login",
      "format": "date-time"
    },
    "ipAddress": {
      "type": "string",
      "description": "IP address of the client",
      "format": "ipv4"
    },
    "userAgent": {
      "type": "string",
      "description": "User agent string from the client"
    },
    "sessionId": {
      "type": "string",
      "description": "Unique session identifier"
    },
    "authenticationMethod": {
      "type": "string",
      "description": "Method used for authentication",
      "enum": ["password", "oauth", "sso", "mfa", "biometric"]
    },
    "success": {
      "type": "boolean",
      "description": "Whether the login was successful"
    },
    "failureReason": {
      "type": "string",
      "description": "Reason for login failure if applicable"
    }
  },
  "required": ["userId", "username", "loginTime", "success"],
  "additionalProperties": false
}',
'Event emitted when a user successfully logs in', 
TRUE, 
'user,login,authentication,security', 
'userId,username,loginTime,success', 
'ipAddress,userAgent,sessionId,authenticationMethod,failureReason', 
'system');

-- System Alert Event Schema
INSERT INTO event_schemas (event_type, version, schema_json, description, active, tags, required_fields, optional_fields, created_by) VALUES
('SYSTEM_ALERT', '1.0', 
'{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "type": "object",
  "title": "System Alert Event",
  "description": "Event emitted for system-level alerts and notifications",
  "properties": {
    "alertId": {
      "type": "string",
      "description": "Unique identifier for the alert"
    },
    "severity": {
      "type": "string",
      "description": "Alert severity level",
      "enum": ["LOW", "MEDIUM", "HIGH", "CRITICAL", "EMERGENCY"]
    },
    "category": {
      "type": "string",
      "description": "Category of the alert",
      "enum": ["SECURITY", "PERFORMANCE", "AVAILABILITY", "ERROR", "WARNING", "INFO"]
    },
    "title": {
      "type": "string",
      "description": "Short title describing the alert",
      "maxLength": 200
    },
    "message": {
      "type": "string",
      "description": "Detailed message about the alert"
    },
    "source": {
      "type": "string",
      "description": "System component that generated the alert"
    },
    "timestamp": {
      "type": "string",
      "description": "ISO 8601 timestamp when alert was generated",
      "format": "date-time"
    },
    "acknowledged": {
      "type": "boolean",
      "description": "Whether the alert has been acknowledged"
    },
    "acknowledgedBy": {
      "type": "string",
      "description": "User who acknowledged the alert"
    },
    "acknowledgedAt": {
      "type": "string",
      "description": "ISO 8601 timestamp when alert was acknowledged",
      "format": "date-time"
    },
    "resolved": {
      "type": "boolean",
      "description": "Whether the alert has been resolved"
    },
    "resolvedBy": {
      "type": "string",
      "description": "User who resolved the alert"
    },
    "resolvedAt": {
      "type": "string",
      "description": "ISO 8601 timestamp when alert was resolved",
      "format": "date-time"
    },
    "metadata": {
      "type": "object",
      "description": "Additional metadata about the alert",
      "additionalProperties": true
    }
  },
  "required": ["alertId", "severity", "category", "title", "message", "source", "timestamp"],
  "additionalProperties": false
}',
'Event emitted for system-level alerts and notifications', 
TRUE, 
'system,alert,notification,monitoring', 
'alertId,severity,category,title,message,source,timestamp', 
'acknowledged,acknowledgedBy,acknowledgedAt,resolved,resolvedBy,resolvedAt,metadata', 
'system');

-- Data Change Event Schema
INSERT INTO event_schemas (event_type, version, schema_json, description, active, tags, required_fields, optional_fields, created_by) VALUES
('DATA_CHANGE', '1.0', 
'{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "type": "object",
  "title": "Data Change Event",
  "description": "Event emitted when data is created, updated, or deleted",
  "properties": {
    "changeId": {
      "type": "string",
      "description": "Unique identifier for the change"
    },
    "operation": {
      "type": "string",
      "description": "Type of data operation",
      "enum": ["CREATE", "UPDATE", "DELETE", "UPSERT", "BATCH_UPDATE", "BATCH_DELETE"]
    },
    "entityType": {
      "type": "string",
      "description": "Type of entity being changed",
      "maxLength": 100
    },
    "entityId": {
      "type": "string",
      "description": "Identifier of the affected entity"
    },
    "tableName": {
      "type": "string",
      "description": "Database table name if applicable"
    },
    "previousData": {
      "type": "object",
      "description": "Previous state of the data before change"
    },
    "newData": {
      "type": "object",
      "description": "New state of the data after change"
    },
    "changedFields": {
      "type": "array",
      "description": "List of field names that were changed",
      "items": {
        "type": "string"
      }
    },
    "timestamp": {
      "type": "string",
      "description": "ISO 8601 timestamp when change occurred",
      "format": "date-time"
    },
    "userId": {
      "type": "string",
      "description": "User who performed the change"
    },
    "sessionId": {
      "type": "string",
      "description": "Session identifier when change was made"
    },
    "transactionId": {
      "type": "string",
      "description": "Database transaction identifier"
    },
    "reason": {
      "type": "string",
      "description": "Reason for the data change",
      "maxLength": 500
    },
    "metadata": {
      "type": "object",
      "description": "Additional metadata about the change",
      "additionalProperties": true
    }
  },
  "required": ["changeId", "operation", "entityType", "entityId", "timestamp"],
  "additionalProperties": false
}',
'Event emitted when data is created, updated, or deleted', 
TRUE, 
'data,change,audit,crud', 
'changeId,operation,entityType,entityId,timestamp', 
'tableName,previousData,newData,changedFields,userId,sessionId,transactionId,reason,metadata', 
'system');

-- API Access Event Schema
INSERT INTO event_schemas (event_type, version, schema_json, description, active, tags, required_fields, optional_fields, created_by) VALUES
('API_ACCESS', '1.0', 
'{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "type": "object",
  "title": "API Access Event",
  "description": "Event emitted for API access and usage tracking",
  "properties": {
    "requestId": {
      "type": "string",
      "description": "Unique identifier for the API request"
    },
    "userId": {
      "type": "string",
      "description": "User making the API request"
    },
    "endpoint": {
      "type": "string",
      "description": "API endpoint being accessed"
    },
    "method": {
      "type": "string",
      "description": "HTTP method used",
      "enum": ["GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS"]
    },
    "statusCode": {
      "type": "integer",
      "description": "HTTP status code returned",
      "minimum": 100,
      "maximum": 599
    },
    "requestTime": {
      "type": "string",
      "description": "ISO 8601 timestamp when request was made",
      "format": "date-time"
    },
    "responseTime": {
      "type": "string",
      "description": "ISO 8601 timestamp when response was sent",
      "format": "date-time"
    },
    "duration": {
      "type": "integer",
      "description": "Request duration in milliseconds"
    },
    "ipAddress": {
      "type": "string",
      "description": "IP address of the client",
      "format": "ipv4"
    },
    "userAgent": {
      "type": "string",
      "description": "User agent string from the client"
    },
    "requestSize": {
      "type": "integer",
      "description": "Size of request payload in bytes"
    },
    "responseSize": {
      "type": "integer",
      "description": "Size of response payload in bytes"
    },
    "success": {
      "type": "boolean",
      "description": "Whether the request was successful"
    },
    "errorMessage": {
      "type": "string",
      "description": "Error message if request failed"
    },
    "rateLimited": {
      "type": "boolean",
      "description": "Whether the request was rate limited"
    },
    "authenticationMethod": {
      "type": "string",
      "description": "Method used for authentication",
      "enum": ["jwt", "oauth", "api_key", "session", "none"]
    },
    "scopes": {
      "type": "array",
      "description": "OAuth scopes if applicable",
      "items": {
        "type": "string"
      }
    },
    "metadata": {
      "type": "object",
      "description": "Additional metadata about the API access",
      "additionalProperties": true
    }
  },
  "required": ["requestId", "endpoint", "method", "statusCode", "requestTime", "success"],
  "additionalProperties": false
}',
'Event emitted for API access and usage tracking', 
TRUE, 
'api,access,usage,monitoring,security', 
'requestId,endpoint,method,statusCode,requestTime,success', 
'userId,responseTime,duration,ipAddress,userAgent,requestSize,responseSize,errorMessage,rateLimited,authenticationMethod,scopes,metadata', 
'system');
