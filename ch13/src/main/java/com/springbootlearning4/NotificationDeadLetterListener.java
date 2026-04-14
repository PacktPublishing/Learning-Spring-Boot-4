package com.springbootlearning4;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationDeadLetterListener {

    @KafkaListener(topics = "employee-events-dlt", groupId = "notification-dlt-group")
    public void handleDeadLetter(EmployeeCreatedEvent event) {
        System.err.println("Message sent to dead-letter topic for employee: " + event.employeeId());
    }
}
