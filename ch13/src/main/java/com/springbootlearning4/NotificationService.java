package com.springbootlearning4;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {

    private final Set<Long> processedEvents = ConcurrentHashMap.newKeySet();

    @KafkaListener(topics = "employee-events", groupId = "notification-group")
    public void handleEmployeeCreated(EmployeeCreatedEvent event) {
        if (!processedEvents.add(event.employeeId())) {
            System.out.println("Skipping duplicate event. Employee ID: " + event.employeeId());
            return;
        }
        sendNotification(event);
    }

    private void sendNotification(EmployeeCreatedEvent event) {
        if (Math.random() < 0.5) {
            throw new IllegalStateException("Temporary network failure");
        }
        if (event.email() == null || event.email().isBlank()) {
            throw new IllegalStateException("Employee email is missing");
        }
        System.out.println("Sending notification to: " + event.email());
    }

}