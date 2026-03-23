package com.springbootlearning4;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NotificationService {

    private final RestClient restClient;

    public NotificationService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://localhost:8080").build();
    }

    public void notifyEmployee(Employee employee) {
        restClient.post()
                .uri("/notify")
                .body(employee)
                .retrieve()
                .toBodilessEntity();

        System.out.println("Notification sent for: " + employee.getName() +
                " | Thread: " + Thread.currentThread() +
                " | isVirtual: " + Thread.currentThread().isVirtual());
    }
}