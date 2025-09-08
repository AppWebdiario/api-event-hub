# WebDiario Event Hub

Sistema centralizado para tratamento, validação, armazenamento e distribuição de eventos usando AWS SQS, SNS e armazenamento estruturado para consulta posterior.

## 🚀 Funcionalidades

### Core Features
- **Processamento de Eventos**: Recebe, valida e processa eventos de diferentes origens
- **Validação JSON Schema**: Validação automática de eventos usando schemas JSON configuráveis
- **Armazenamento Estruturado**: Persistência de eventos em banco MySQL com histórico completo
- **Distribuição AWS**: Integração com SQS para filas e SNS para tópicos
- **Monitoramento**: Rastreamento completo do processamento com métricas de performance

### Event Management
- **Schema Management**: Criação e versionamento de schemas JSON para diferentes tipos de eventos
- **Event Validation**: Validação automática contra schemas configurados
- **Event Routing**: Roteamento inteligente baseado em tipo, prioridade e origem
- **Retry Logic**: Sistema de retry automático com backoff exponencial
- **Dead Letter Queue**: Tratamento de eventos que falharam após todas as tentativas

### Storage & Query
- **Event Persistence**: Armazenamento completo de eventos com metadados
- **Processing History**: Histórico detalhado de todas as tentativas de processamento
- **Advanced Queries**: Consultas complexas por tipo, status, período, usuário, etc.
- **Data Retention**: Política configurável de retenção de dados
- **Audit Trail**: Rastreamento completo de mudanças e processamento

## 🏗️ Arquitetura

### Componentes Principais
```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Event Input   │───▶│  Event Validator │───▶│ Event Processor │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                │                        │
                                ▼                        ▼
                       ┌──────────────────┐    ┌─────────────────┐
                       │ Schema Registry  │    │   AWS SQS/SNS   │
                       └──────────────────┘    └─────────────────┘
                                │                        │
                                ▼                        ▼
                       ┌──────────────────┐    ┌─────────────────┐
                       │ Event Storage   │    │ Event Router    │
                       └──────────────────┘    └─────────────────┘
```

### Tecnologias Utilizadas
- **Spring Boot 3.2.0**: Framework base da aplicação
- **Spring Data JPA**: Persistência de dados
- **MySQL 8.0**: Banco de dados principal
- **Liquibase**: Migrações de banco de dados
- **AWS SDK**: Integração com serviços AWS (SQS, SNS)
- **JSON Schema Validator**: Validação de eventos
- **Spring Cloud AWS**: Integração com AWS Messaging

## 📊 Estrutura do Banco de Dados

### Tabelas Principais

#### `eventos`
- Armazena todos os eventos processados
- Metadados completos (tipo, origem, versão, payload, etc.)
- Status de processamento e tentativas
- Informações de performance e auditoria

#### `event_schemas`
- Schemas JSON para validação de eventos
- Versionamento e controle de deprecação
- Metadados de validação e regras de negócio
- Exemplos e guias de migração

#### `event_processing_history`
- Histórico completo de processamento
- Métricas de performance (duração, memória, CPU)
- Rastreamento de erros e tentativas
- Informações de contexto (IP, user agent, etc.)

## 🔧 Configuração

### Variáveis de Ambiente
```yaml
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/webdiario_event_hub
spring.datasource.username=root
spring.datasource.password=root

# AWS Configuration
aws.region=us-east-1
aws.sqs.endpoint=http://localhost:4566  # LocalStack for development
aws.sns.endpoint=http://localhost:4566

# Event Hub Configuration
event-hub.batch-size=100
event-hub.retry-attempts=3
event-hub.retry-delay=1000
event-hub.event-retention-days=90
```

### Configuração de Filas SQS
```yaml
aws.sqs.queues:
  user-events: webdiario-user-events
  system-events: webdiario-system-events
  notification-events: webdiario-notification-events
```

### Configuração de Tópicos SNS
```yaml
aws.sns.topics:
  user-notifications: webdiario-user-notifications
  system-alerts: webdiario-system-alerts
  audit-logs: webdiario-audit-logs
```

## 📝 Schemas de Eventos

### Eventos Padrão Implementados

#### USER_CREATED
```json
{
  "userId": "string",
  "username": "string",
  "email": "string",
  "fullName": "string",
  "roles": ["string"],
  "tenantId": "string",
  "metadata": "object"
}
```

#### USER_UPDATED
```json
{
  "userId": "string",
  "updatedFields": ["string"],
  "previousValues": "object",
  "newValues": "object",
  "updatedBy": "string",
  "reason": "string"
}
```

#### USER_LOGIN
```json
{
  "userId": "string",
  "username": "string",
  "loginTime": "datetime",
  "ipAddress": "string",
  "userAgent": "string",
  "sessionId": "string",
  "authenticationMethod": "string",
  "success": "boolean"
}
```

#### SYSTEM_ALERT
```json
{
  "alertId": "string",
  "severity": "string",
  "category": "string",
  "title": "string",
  "message": "string",
  "source": "string",
  "timestamp": "datetime"
}
```

#### DATA_CHANGE
```json
{
  "changeId": "string",
  "operation": "string",
  "entityType": "string",
  "entityId": "string",
  "timestamp": "datetime",
  "previousData": "object",
  "newData": "object"
}
```

#### API_ACCESS
```json
{
  "requestId": "string",
  "endpoint": "string",
  "method": "string",
  "statusCode": "integer",
  "requestTime": "datetime",
  "success": "boolean",
  "duration": "integer"
}
```

## 🚀 Como Executar

### Pré-requisitos
- Java 21+
- Maven 3.8+
- MySQL 8.0+
- Docker (para LocalStack)

### Execução Local
```bash
# Clone o repositório
git clone <repository-url>
cd api-webdiario-event-hub

# Configure o banco de dados
# Crie o banco: webdiario_event_hub

# Execute a aplicação
mvn spring-boot:run
```

### Execução com Docker
```bash
# Inicie o LocalStack para desenvolvimento
docker-compose up -d

# Execute a aplicação
mvn spring-boot:run
```

## 📡 Endpoints da API

### Eventos
- `POST /api/event-hub/events` - Criar novo evento
- `GET /api/event-hub/events` - Listar eventos
- `GET /api/event-hub/events/{id}` - Buscar evento por ID
- `GET /api/event-hub/events/search` - Busca avançada de eventos
- `DELETE /api/event-hub/events/{id}` - Remover evento

### Schemas
- `POST /api/event-hub/schemas` - Criar novo schema
- `GET /api/event-hub/schemas` - Listar schemas
- `GET /api/event-hub/schemas/{tipoEvento}/{versao}` - Buscar schema
- `PUT /api/event-hub/schemas/{id}` - Atualizar schema
- `DELETE /api/event-hub/schemas/{id}` - Remover schema

### Processamento
- `GET /api/event-hub/processing/history/{eventId}` - Histórico de processamento
- `POST /api/event-hub/processing/retry/{eventId}` - Reprocessar evento
- `GET /api/event-hub/processing/stats` - Estatísticas de processamento

### Monitoramento
- `GET /api/event-hub/health` - Status da aplicação
- `GET /api/event-hub/metrics` - Métricas de performance
- `GET /api/event-hub/queues/status` - Status das filas SQS

## 🔍 Consultas e Filtros

### Filtros por Evento
```java
// Por tipo de evento
List<Evento> eventos = eventoRepository.findByTipoEvento("USER_CREATED");

// Por origem
List<Evento> eventos = eventoRepository.findByOrigem("webdiario-security");

// Por status
List<Evento> eventos = eventoRepository.findByStatus(EventStatus.PROCESSED);

// Por período
List<Evento> eventos = eventoRepository.findByTimestampEventoBetween(
    LocalDateTime.now().minusDays(7), 
    LocalDateTime.now()
);

// Por usuário
List<Evento> eventos = eventoRepository.findByUserId("user123");

// Por tenant
List<Evento> eventos = eventoRepository.findByTenantId("tenant1");
```

### Busca Avançada
```java
// Busca por conteúdo no payload ou metadata
Page<Evento> eventos = eventoRepository.findByPayloadOrMetadataContaining(
    "searchTerm", 
    PageRequest.of(0, 20)
);

// Eventos com erros
List<Evento> eventos = eventoRepository.findEventsWithErrors();

// Eventos expirados
List<Evento> eventos = eventoRepository.findExpiredEvents(LocalDateTime.now());

// Eventos para retry
List<Evento> eventos = eventoRepository.findRetryableEvents(LocalDateTime.now());
```

### Estatísticas
```java
// Contagem por tipo de evento
List<Object[]> stats = eventoRepository.countByTipoEvento();

// Contagem por status
List<Object[]> stats = eventoRepository.countByStatus();

// Tempo médio de processamento
Double avgTime = eventoRepository.getAverageProcessingTime();

// Tempo máximo de processamento
Long maxTime = eventoRepository.getMaxProcessingTime();
```

## 🔄 Sistema de Retry

### Configuração
```yaml
event-hub:
  retry-attempts: 3
  retry-delay: 1000  # milliseconds
  max-tentativas: 3
```

### Lógica de Retry
1. **Primeira tentativa**: Processamento imediato
2. **Segunda tentativa**: Delay de 1 segundo
3. **Terceira tentativa**: Delay de 2 segundos
4. **Dead Letter Queue**: Após todas as tentativas

### Configuração de Dead Letter Queue
```yaml
event-hub:
  dead-letter-queue: webdiario-dlq
```

## 📊 Monitoramento e Métricas

### Métricas Disponíveis
- **Eventos por segundo**: Taxa de processamento
- **Tempo de processamento**: Média, mínimo, máximo
- **Taxa de erro**: Percentual de eventos com falha
- **Uso de recursos**: Memória, CPU, tempo de resposta
- **Status das filas**: Tamanho, latência, throughput

### Health Checks
- **Database**: Conexão com MySQL
- **AWS Services**: Status de SQS e SNS
- **Event Processing**: Status dos processadores
- **Schema Validation**: Status do validador JSON

## 🚀 Deploy e Produção

### Configuração de Produção
```yaml
# AWS Production
aws:
  region: us-east-1
  sqs:
    endpoint: https://sqs.us-east-1.amazonaws.com
  sns:
    endpoint: https://sns.us-east-1.amazonaws.com

# Performance
event-hub:
  batch-size: 500
  retry-attempts: 5
  event-retention-days: 365
  max-event-size: 5242880  # 5MB
```

### Escalabilidade
- **Horizontal Scaling**: Múltiplas instâncias da aplicação
- **Queue Partitioning**: Partição de filas por tipo de evento
- **Database Sharding**: Sharding por tenant ou período
- **Caching**: Cache Redis para schemas e metadados

### Monitoramento em Produção
- **CloudWatch**: Métricas AWS e logs
- **Prometheus + Grafana**: Métricas customizadas
- **ELK Stack**: Logs centralizados
- **Sentry**: Rastreamento de erros

## 🧪 Testes

### Testes Unitários
```bash
mvn test
```

### Testes de Integração
```bash
mvn test -Dtest=*IntegrationTest
```

### Testes com TestContainers
```bash
mvn test -Dtest=*ContainerTest
```

## 📚 Documentação Adicional

- [API Documentation](http://localhost:8082/api/event-hub/swagger-ui.html)
- [Database Schema](docs/database-schema.md)
- [Event Schemas](docs/event-schemas.md)
- [AWS Integration](docs/aws-integration.md)
- [Performance Tuning](docs/performance-tuning.md)

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 🆘 Suporte

Para suporte e dúvidas:
- Abra uma issue no GitHub
- Entre em contato com a equipe de desenvolvimento
- Consulte a documentação da API
