package com.springbootlearning4;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EmployeeRepository extends //
  ReactiveCrudRepository<Employee, Long> {}
