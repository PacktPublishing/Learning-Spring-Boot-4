package com.learningspringboot4;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationDeadLetterListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationDeadLetterListener.class);
    private final ObservationRegistry observationRegistry;

    public NotificationDeadLetterListener(ObservationRegistry observationRegistry) {
        this.observationRegistry = observationRegistry;
    }

    @KafkaListener(topics = "employee-events-dlt", groupId = "notification-dlt-group")
    public void handleDeadLetter(EmployeeCreatedEvent event) {
        Observation
                .createNotStarted("notification.dead-letter", observationRegistry)
                .contextualName("process dead-letter notification")
                .lowCardinalityKeyValue("messaging.destination.name", "employee-events-dlt")
                .highCardinalityKeyValue("employee.id", String.valueOf(event.employeeId()))
                .observe(() -> log.error("Message sent to dead-letter topic for employee {}", event.employeeId()));
    }
}
