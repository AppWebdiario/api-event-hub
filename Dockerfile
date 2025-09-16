# Runtime stage for WebDiário Event Hub API
FROM webdiario/alpine/local/java/21:latest

USER root
# Set application environment variables
ENV APP_NAME=webdiario-event-hub
ENV APP_PORT=8083

# Use existing app user from base image
# (appuser already exists in webdiario/alpine/local/java/21)

# Set working directory
WORKDIR /app

# Copy the built jar from pipeline build
COPY target/api-webdiario-event-hub-*.jar app.jar

# Change ownership to app user
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose application port and JMX Exporter port
EXPOSE 8083 9404

# Set JVM options for production
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport"

# Set JMX Exporter configuration
ENV JMX_EXPORTER_PORT=9404
ENV JMX_EXPORTER_CONFIG=/app/jmx_prometheus_config.yml

# Run the application using the start script with JMX Exporter
ENTRYPOINT ["/usr/local/bin/start-java-app.sh"]
