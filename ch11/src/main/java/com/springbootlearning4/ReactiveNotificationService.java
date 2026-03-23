package com.springbootlearning4;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ReactiveNotificationService {

    private final WebClient webClient;

    public ReactiveNotificationService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8080").build();
    }

    public void notifyEmployee(Employee employee) {
        webClient.post()
                .uri("/notify")
                .bodyValue(employee)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        System.out.println("Reactive notification sent for: " + employee.getName() +
                " | Thread: " + Thread.currentThread() +
                " | isVirtual: " + Thread.currentThread().isVirtual());
    }
}