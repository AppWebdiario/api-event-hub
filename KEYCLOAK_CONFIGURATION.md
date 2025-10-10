# Configuração Keycloak - API Event Hub

## Resumo
Este documento descreve as configurações realizadas para integrar a API Event Hub com o Keycloak para autenticação e autorização.

## Data da Configuração
08 de Outubro de 2025

## Credenciais do Cliente Keycloak

```
Client ID: api-event-hub-client
Name: api-event-hub Client
Client Secret: DcfhBb0Ykbie00UlAoFxzSN82NdVIiWZ
Realm: master
Server URL: https://keycloak.appwebdiario.com.br
```

## Alterações Realizadas

### 1. Dependências Maven (pom.xml)

Adicionadas as seguintes dependências:

```xml
<!-- Spring Security & OAuth2 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>

<!-- WebDiário Security Spring Boot Starter -->
<dependency>
    <groupId>br.com.tiagoramos.webdiario</groupId>
    <artifactId>webdiario-security-spring-starter</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- Keycloak Admin Client -->
<dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-admin-client</artifactId>
    <version>26.0.7</version>
</dependency>
```

### 2. Configuração da Aplicação (application.yml)

Configuração do Keycloak no arquivo `src/main/resources/application.yml`:

```yaml
webdiario:
  security:
    resource-server: true
    cors:
      enabled: true
      allowed-origins: "*"
      allowed-methods: GET,POST,PUT,DELETE,OPTIONS,PATCH,HEAD
      allowed-headers: "*"
      allow-credentials: true
      exposed-headers: Authorization,Content-Type,X-Requested-With,Access-Control-Allow-Origin,Access-Control-Allow-Credentials
      max-age: 3600
    tenant:
      enabled: true
      default-tenant: 1
      tenant-header: "X-Tenant-ID"
      validate-user-access: true
    audit:
      enabled: true
      log-login: true
      log-logout: true
      log-sensitive-operations: true
      log-auth-failures: true

  keycloak:
    enabled: true
    server-url: ${KEYCLOAK_SERVER_URL:https://keycloak.appwebdiario.com.br}
    realm: ${KEYCLOAK_REALM:master}
    client-id: ${OAUTH_CLIENT_ID:api-event-hub-client}
    client-secret: ${OAUTH_CLIENT_SECRET:DcfhBb0Ykbie00UlAoFxzSN82NdVIiWZ}
    app-name: ${OAUTH_APP_NAME:api-event-hub}
    organization:
      validate-user-access: true
    jwks:
      cache-enabled: true
      cache-ttl: 3600
      cache-refresh: 300
      max-retries: 3
      timeout: 5000
```

### 3. Configuração Alternativa (application-keycloak.yml)

O arquivo `application-keycloak.yml` também foi atualizado com as mesmas credenciais para uso em ambientes específicos.

### 4. Classe Principal (EventHubApplication.java)

Adicionada a anotação `@EnableWebdiarioSecurity`:

```java
@SpringBootApplication
@EntityScan("com.webdiario.eventhub.entity")
@EnableJpaRepositories("com.webdiario.eventhub.repository")
@EnableAsync
@EnableScheduling
@EnableWebdiarioSecurity  // ← Nova anotação
public class EventHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventHubApplication.class, args);
    }
}
```

### 5. Configuração de Segurança (SecurityConfig.java)

Criado o arquivo `src/main/java/com/webdiario/eventhub/config/SecurityConfig.java`:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain oauth2ResourceServerFilterChain(HttpSecurity http,
            TenantContextFilter tenantContextFilter,
            JwtDecoder jwtDecoder,
            KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter,
            @Qualifier("webdiarioCorsConfigurationSource") CorsConfigurationSource corsConfigurationSource)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**", "/swagger-ui/**", "/api-docs/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(tenantContextFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder)
                                .jwtAuthenticationConverter(keycloakJwtAuthenticationConverter)));
        return http.build();
    }
}
```

## Funcionalidades Habilitadas

### 1. Autenticação OAuth2/JWT
- Token JWT validado através do Keycloak
- Conversão de claims do Keycloak para authorities do Spring Security
- Cache de JWKS para melhor performance

### 2. Multi-tenancy
- Suporte a múltiplos tenants
- Header `X-Tenant-ID` para identificação do tenant
- Validação de acesso do usuário ao tenant

### 3. CORS
- Configuração de CORS habilitada
- Permite origins, methods e headers configuráveis
- Suporte para credentials

### 4. Auditoria
- Log de login/logout
- Log de operações sensíveis
- Log de falhas de autenticação

### 5. Endpoints Públicos
- `/actuator/**` - Endpoints de monitoramento
- `/swagger-ui/**` - Interface do Swagger
- `/api-docs/**` - Documentação da API

## Variáveis de Ambiente

As seguintes variáveis de ambiente podem ser usadas para sobrescrever as configurações padrão:

```bash
KEYCLOAK_SERVER_URL=https://keycloak.appwebdiario.com.br
KEYCLOAK_REALM=master
OAUTH_CLIENT_ID=api-event-hub-client
OAUTH_CLIENT_SECRET=DcfhBb0Ykbie00UlAoFxzSN82NdVIiWZ
OAUTH_APP_NAME=api-event-hub
```

## Testes

Para testar a configuração:

1. **Obter um token do Keycloak:**
```bash
curl -X POST "https://keycloak.appwebdiario.com.br/realms/master/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=api-event-hub-client" \
  -d "client_secret=DcfhBb0Ykbie00UlAoFxzSN82NdVIiWZ" \
  -d "grant_type=client_credentials"
```

2. **Usar o token para acessar a API:**
```bash
curl -X GET "http://localhost:8083/api/events" \
  -H "Authorization: Bearer <seu-token-aqui>" \
  -H "X-Tenant-ID: 1"
```

## Próximos Passos

1. Configurar roles e permissões específicas no Keycloak
2. Implementar autorização baseada em roles nos endpoints
3. Configurar refresh token se necessário
4. Implementar logout centralizado

## Referências

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Spring Security OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
- WebDiário Security Starter - Módulo interno de segurança
