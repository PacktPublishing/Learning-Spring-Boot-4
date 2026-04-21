package com.springbootlearning4;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final MeterRegistry meterRegistry;
    private final ObservationRegistry observationRegistry;
    private final Set<Long> processedEvents = ConcurrentHashMap.newKeySet();

    public NotificationService(MeterRegistry meterRegistry, ObservationRegistry observationRegistry) {
        this.meterRegistry = meterRegistry;
        this.observationRegistry = observationRegistry;
    }

    @KafkaListener(topics = "employee-events", groupId = "notification-group")
    public void handleEmployeeCreated(EmployeeCreatedEvent event) {

        /*
            recordNotificationMetric("received");
            log.info("Received employee-created event for employee {}", event.employeeId());
            if (!processedEvents.add(event.employeeId())) {
                recordNotificationMetric("duplicate");
                log.info("Skipping duplicate employee-created event for employee {}", event.employeeId());
                return;
            }
            sendNotification(event);
         */

        Observation
                .createNotStarted("notification.process", observationRegistry)
                .contextualName("process employee notification")
                .lowCardinalityKeyValue("messaging.destination.name", "employee-events")
                .highCardinalityKeyValue("employee.id", String.valueOf(event.employeeId()))
                .observe(() -> {
                    recordNotificationMetric("received");
                    log.info("Received employee-created event for employee {}", event.employeeId());
                    if (!processedEvents.add(event.employeeId())) {
                        recordNotificationMetric("duplicate");
                        log.info("Skipping duplicate employee-created event for employee {}", event.employeeId());
                        return;
                    }
                    sendNotification(event);
                });
    }

    private void sendNotification(EmployeeCreatedEvent event) {
        if (Math.random() < 0.5) {
            recordNotificationMetric("failed");
            log.warn("Simulating temporary notification failure for employee {}", event.employeeId());
            throw new IllegalStateException("Temporary network failure");
        }
        if (event.email() == null || event.email().isBlank()) {
            recordNotificationMetric("failed");
            log.warn("Cannot send notification for employee {} because email is missing", event.employeeId());
            throw new IllegalStateException("Employee email is missing");
        }
        recordNotificationMetric("sent");
        log.info("Sending notification to {}", event.email());
    }

    private void recordNotificationMetric(String outcome) {
        meterRegistry.counter("employee.notification.count", "outcome", outcome).increment();
    }

}
