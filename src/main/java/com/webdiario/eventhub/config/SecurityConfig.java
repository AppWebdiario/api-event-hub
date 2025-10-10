package com.webdiario.eventhub.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import br.com.tiagoramos.webdiario.security.starter.security.KeycloakJwtAuthenticationConverter;
import br.com.tiagoramos.webdiario.security.starter.tenant.TenantContextFilter;

/**
 * Configuração de segurança personalizada para a aplicação Event Hub
 */
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
