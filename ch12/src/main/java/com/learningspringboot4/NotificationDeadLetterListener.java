package com.learningspringboot4;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class NotificationDeadLetterListener {

    @KafkaListener(topics = "employee-events-dlt", groupId = "notification-dlt-group")
    public void handleDeadLetter(ConsumerRecord<String, byte[]> record) {
        byte[] payload = record.value(); System.err.println("Message sent to dead-letter topic.");

        System.err.println("Topic: " + record.topic());
        System.err.println("Partition: " + record.partition());
        System.err.println("Offset: " + record.offset());
        System.err.println("Payload: " + new String(payload, StandardCharsets.UTF_8));
    }
}
