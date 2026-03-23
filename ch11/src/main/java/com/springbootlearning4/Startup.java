package com.springbootlearning4;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Startup {

  @Bean
  CommandLineRunner initDatabase(EmployeeRepository repository) {
    return args -> {
      if (repository.count() == 0) {
        repository.saveAll(List.of(
            new Employee("Frodo Baggins", "ring bearer"),
            new Employee("Samwise Gamgee", "gardener"),
            new Employee("Bilbo Baggins", "burglar")
        ));
      }
    };
  }
}
