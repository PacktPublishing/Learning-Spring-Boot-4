package com.springbootlearning4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationDeadLetterListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationDeadLetterListener.class);

    @KafkaListener(topics = "employee-events-dlt", groupId = "notification-dlt-group")
    public void handleDeadLetter(EmployeeCreatedEvent event) {
        log.error("Employee-created event moved to dead-letter topic for employee {}", event.employeeId());
    }
}
