package com.springbootlearning4;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NotificationClientService {

    private final NotificationClient notificationClient;

    public NotificationClientService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public void notifyEmployee(Employee employee) {
        notificationClient.notifyEmployee(employee);

        System.out.println("Notification sent for: " + employee.getName() +
                " | Thread: " + Thread.currentThread() +
                " | isVirtual: " + Thread.currentThread().isVirtual());
    }
}
