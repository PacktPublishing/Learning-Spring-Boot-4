package com.learningspringboot4;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;
    private final KafkaTemplate<String, EmployeeCreatedEvent> kafkaTemplate;

    public EmployeeService(EmployeeRepository employeeRepository, KafkaTemplate<String, EmployeeCreatedEvent> kafkaTemplate) {
        this.employeeRepository = employeeRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Employee createEmployee(Employee employee) {

        Employee saved = employeeRepository.save(employee);

        EmployeeCreatedEvent employeeCreatedEvent = new EmployeeCreatedEvent(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                LocalDateTime.now()
        );

        kafkaTemplate.send("employee-events", saved.getId().toString(), employeeCreatedEvent);

        return saved;
    }
}