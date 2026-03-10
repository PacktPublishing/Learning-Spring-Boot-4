package com.springbootlearning4;

import org.springframework.data.annotation.Id;

public record Employee(
        @Id Long id,
        String name,
        String role
) {
  public Employee(String name, String role) {
    this(null, name, role);
  }
}