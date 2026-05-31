package com.learningspringboot4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        log.info("Received request to create employee with role {}", roleForLog(employee));
        Employee saved = service.createEmployee(employee);
        log.info("Returning created employee {}", saved.getId());
        return ResponseEntity.ok(saved);
    }

    private String roleForLog(Employee employee) {
        if (employee.getRole() == null || employee.getRole().isBlank()) {
            return "UNKNOWN";
        }
        return employee.getRole();
    }
}
