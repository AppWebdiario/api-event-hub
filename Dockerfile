# Runtime stage for WebDiário API
FROM registry.appwebdiario.com.br/appwebdiario/alpine/openjdk:latest

USER root

# Set application environment variables
ENV APP_NAME=api-event-hub
ENV APP_PORT=8080

# Set working directory
WORKDIR /app

# Copy the built jar from pipeline build
COPY ./target/*.jar app.jar

# Change ownership to app user
RUN chown -R appwebdiario:appwebdiario /app

# Switch to non-root user
USER appwebdiario

# Expose application port and JMX Exporter port
EXPOSE 8080 9404

# Set JVM options for production
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport"

# Set JMX Exporter configuration
ENV JMX_EXPORTER_PORT=9404
ENV JMX_EXPORTER_CONFIG=/app/jmx_prometheus_config.yml

# Set the default command to run the Spring Boot application
CMD ["java", "-jar", "/app/app.jar"]
