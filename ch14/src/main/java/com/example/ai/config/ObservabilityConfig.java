package com.example.ai.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Heading 5 — wires the OpenTelemetry Logback appender so structured log events
 * are exported through the same OTLP pipeline as metrics and traces.
 * Pattern mirrors Chapter 13's ObservabilityConfig.
 */
@Configuration
public class ObservabilityConfig {

    @Bean
    ApplicationRunner openTelemetryLogbackAppenderInstaller(OpenTelemetry openTelemetry) {
        return args -> OpenTelemetryAppender.install(openTelemetry);
    }
}
