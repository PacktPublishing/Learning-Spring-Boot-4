package com.learningspringboot4;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
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
    private final MeterRegistry meterRegistry;
    private final ObservationRegistry observationRegistry;

    public EmployeeService(EmployeeRepository employeeRepository,
                           KafkaTemplate<String, EmployeeCreatedEvent> kafkaTemplate,
                           MeterRegistry meterRegistry,
                           ObservationRegistry observationRegistry) {
        this.employeeRepository = employeeRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.meterRegistry = meterRegistry;
        this.observationRegistry = observationRegistry;
    }

    public Employee createEmployee(Employee employee) {
        String role = roleForMetrics(employee);

        // Metrics only
        /*
        return Timer
                .builder("employee.create.time")
                .description("Time taken to create an employee")
                .tag("role", role)
                .register(meterRegistry)
                .record(() -> createEmployeeAndPublishEvent(employee, role));
        */

        return Observation
                .createNotStarted("employee.create", observationRegistry)
                .contextualName("create employee")
                .lowCardinalityKeyValue("employee.role", role)
                .observe(() -> Timer
                        .builder("employee.create.time")
                        .description("Time taken to create an employee")
                        .tag("role", role)
                        .register(meterRegistry)
                        .record(() -> createEmployeeAndPublishEvent(employee, role)));
    }

    private Employee createEmployeeAndPublishEvent(Employee employee, String role) {
        log.info("Starting employee creation for role {}", role);

        Employee saved = employeeRepository.save(employee);
        meterRegistry.counter("employee.created.count", "role", role).increment();
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

    private String roleForMetrics(Employee employee) {
        if (employee.getRole() == null || employee.getRole().isBlank()) {
            return "UNKNOWN";
        }
        return employee.getRole().toUpperCase();
    }
}
