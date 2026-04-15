package com.springbootlearning4;

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
    private final ObservationRegistry observationRegistry;

    public EmployeeService(EmployeeRepository employeeRepository,
                           KafkaTemplate<String, EmployeeCreatedEvent> kafkaTemplate,
                           ObservationRegistry observationRegistry) {
        this.employeeRepository = employeeRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.observationRegistry = observationRegistry;
    }

    public Employee createEmployee(Employee employee) {
        Observation observation = Observation
                .createNotStarted("employee.create", observationRegistry)
                .lowCardinalityKeyValue("employee.role", roleForMetrics(employee));

        return observation.observe(() -> {
            log.info("Starting employee.create operation for role {}", roleForMetrics(employee));
            if (employee.getCreatedAt() == null) {
                employee.setCreatedAt(LocalDateTime.now());
            }

            Employee saved = employeeRepository.save(employee);
            observation.highCardinalityKeyValue("employee.id", saved.getId().toString());

            EmployeeCreatedEvent employeeCreatedEvent = new EmployeeCreatedEvent(
                    saved.getId(),
                    saved.getName(),
                    saved.getEmail(),
                    saved.getCreatedAt()
            );

            Observation
                    .createNotStarted("employee.event.publish", observationRegistry)
                    .lowCardinalityKeyValue("messaging.destination.name", "employee-events")
                    .highCardinalityKeyValue("employee.id", saved.getId().toString())
                    .observe(() -> kafkaTemplate.send("employee-events", saved.getId().toString(), employeeCreatedEvent));
            log.info("Created employee {} and published employee-created event", saved.getId());
            return saved;
        });
    }

    private String roleForMetrics(Employee employee) {
        if (employee.getRole() == null || employee.getRole().isBlank()) {
            return "UNKNOWN";
        }
        return employee.getRole().toUpperCase();
    }
}
