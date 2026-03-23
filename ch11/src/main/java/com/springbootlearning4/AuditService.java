package com.springbootlearning4;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AuditService {

    private final TaskExecutor taskExecutor;

    public AuditService(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void registerEmployeeCreation(Employee employee) {
        taskExecutor.execute(() -> {
            try {
                if (employee.getName().equalsIgnoreCase("error")) {
                    throw new RuntimeException("Simulated audit failure");
                }
                System.out.println("Audit log for employee: " + employee.getName());
            } catch (Exception ex) {
                System.err.println("Audit failed for employee: " + employee.getName()
                        + " | error: " + ex.getMessage());
            }

            System.out.println("Audit log for employee: " + employee.getName() +
                    " | Thread: " + Thread.currentThread() +
                    " | isVirtual: " + Thread.currentThread().isVirtual());
        });
    }


//    public void registerEmployeeCreation(Employee employee) {
//        CompletableFuture.runAsync(() -> {
//            if (employee.getName().equalsIgnoreCase("error")) {
//                throw new RuntimeException("Simulated audit failure");
//            }
//
//            System.out.println("Audit log for employee: " + employee.getName());
//        }).exceptionally(ex -> {
//            System.err.println("Audit failed: " + ex.getMessage());
//            return null;
//        });
//    }

}