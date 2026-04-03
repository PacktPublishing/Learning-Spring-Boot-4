package com.springbootlearning4;

import java.time.LocalDateTime;

public record EmployeeCreatedEvent(
        Long employeeId,
        String name,
        String email,
        LocalDateTime createdAt
) {}
