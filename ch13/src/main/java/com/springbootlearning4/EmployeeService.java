package com.springbootlearning4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final KafkaTemplate<String, EmployeeCreatedEvent> kafkaTemplate;

    public EmployeeService(EmployeeRepository employeeRepository, KafkaTemplate<String, EmployeeCreatedEvent> kafkaTemplate) {
        this.employeeRepository = employeeRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Employee createEmployee(Employee employee) {
        log.info("Starting employee creation for role {}", roleForLog(employee));

        Employee saved = employeeRepository.save(employee);
        log.info("Employee {} saved to the database", saved.getId());

        EmployeeCreatedEvent employeeCreatedEvent = new EmployeeCreatedEvent(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                LocalDateTime.now()
        );

        kafkaTemplate.send("employee-events", saved.getId().toString(), employeeCreatedEvent);
        log.info("Published employee-created event for employee {}", saved.getId());

        return saved;
    }

    private String roleForLog(Employee employee) {
        if (employee.getRole() == null || employee.getRole().isBlank()) {
            return "UNKNOWN";
        }
        return employee.getRole();
    }
}
