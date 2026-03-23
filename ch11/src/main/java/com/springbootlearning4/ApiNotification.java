package com.springbootlearning4;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiNotification {
    @PostMapping("/notify")
    ResponseEntity<Void> notifyEmployee(@RequestBody Employee employee) {
        System.out.println("Notification received for employee: " + employee.getName() +
                " | Thread: " + Thread.currentThread() +
                " | isVirtual: " + Thread.currentThread().isVirtual());

        return ResponseEntity.ok().build();
    }
}
