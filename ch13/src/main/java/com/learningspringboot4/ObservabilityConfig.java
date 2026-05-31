package com.learningspringboot4;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObservabilityConfig {

    @Bean
    public ApplicationRunner openTelemetryLogbackAppenderInstaller(OpenTelemetry openTelemetry) {
        return args -> OpenTelemetryAppender.install(openTelemetry);
    }

}
