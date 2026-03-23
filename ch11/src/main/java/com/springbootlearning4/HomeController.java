package com.springbootlearning4;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    private final EmployeeRepository repository;
    private final AuditService auditService;
    private final NotificationService notificationService;
    private final ReactiveNotificationService reactiveNotificationService;
    private final NotificationClientService notificationClientService;

    public HomeController(EmployeeRepository repository, AuditService auditService, NotificationService notificationService, ReactiveNotificationService reactiveNotificationService, NotificationClientService notificationClientService) {
        this.repository = repository;
        this.auditService = auditService;
        this.notificationService = notificationService;
        this.reactiveNotificationService = reactiveNotificationService;
        this.notificationClientService = notificationClientService;
    }

    @GetMapping("/")
    String index(Model model) {
        model.addAttribute("employees", repository.findAll());
        model.addAttribute("newEmployee", new Employee("", ""));
        return "index";
    }

    @PostMapping("/new-employee")
    String newEmployee(@ModelAttribute Employee newEmployee) {
        Employee employeeToSave = new Employee(newEmployee.getName(), newEmployee.getRole());
        Employee employeeSaved = repository.save(employeeToSave);
        auditService.registerEmployeeCreation(employeeSaved);
        notificationService.notifyEmployee(employeeSaved);
        reactiveNotificationService.notifyEmployee(employeeSaved);
        notificationClientService.notifyEmployee(employeeSaved);
        return "redirect:/";
    }
}
