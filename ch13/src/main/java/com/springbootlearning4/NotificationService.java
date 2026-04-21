package com.springbootlearning4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final Set<Long> processedEvents = ConcurrentHashMap.newKeySet();

    @KafkaListener(topics = "employee-events", groupId = "notification-group")
    public void handleEmployeeCreated(EmployeeCreatedEvent event) {
        log.info("Received employee-created event for employee {}", event.employeeId());
        if (!processedEvents.add(event.employeeId())) {
            log.info("Skipping duplicate employee-created event for employee {}", event.employeeId());
            return;
        }
        sendNotification(event);
    }

    private void sendNotification(EmployeeCreatedEvent event) {
        if (Math.random() < 0.5) {
            log.warn("Simulating temporary notification failure for employee {}", event.employeeId());
            throw new IllegalStateException("Temporary network failure");
        }
        if (event.email() == null || event.email().isBlank()) {
            log.warn("Cannot send notification for employee {} because email is missing", event.employeeId());
            throw new IllegalStateException("Employee email is missing");
        }
        log.info("Sending notification to {}", event.email());
    }

}
