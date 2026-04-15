package com.springbootlearning4;

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

    private final ObservationRegistry observationRegistry;
    private final Set<Long> processedEvents = ConcurrentHashMap.newKeySet();

    public NotificationService(ObservationRegistry observationRegistry) {
        this.observationRegistry = observationRegistry;
    }

    @KafkaListener(topics = "employee-events", groupId = "notification-group")
    public void handleEmployeeCreated(EmployeeCreatedEvent event) {
        Observation
                .createNotStarted("employee.notification.process", observationRegistry)
                .lowCardinalityKeyValue("messaging.destination.name", "employee-events")
                .highCardinalityKeyValue("employee.id", event.employeeId().toString())
                .observe(() -> processEmployeeCreatedEvent(event));
    }

    private void processEmployeeCreatedEvent(EmployeeCreatedEvent event) {
        log.info("Received employee-created event for employee {}", event.employeeId());
        if (!processedEvents.add(event.employeeId())) {
            log.info("Skipping duplicate employee-created event for employee {}", event.employeeId());
            return;
        }
        sendNotification(event);
    }

    private void sendNotification(EmployeeCreatedEvent event) {
        if (event.email() == null || event.email().isBlank()) {
            log.warn("Cannot send welcome notification for employee {} because email is missing", event.employeeId());
            throw new IllegalStateException("Employee email is missing");
        }
        if (event.email().endsWith("@fail.test")) {
            log.warn("Simulating notification provider timeout for employee {}", event.employeeId());
            throw new IllegalStateException("Simulated notification provider timeout");
        }
        log.info("Sending welcome notification to {}", event.email());
    }

}
