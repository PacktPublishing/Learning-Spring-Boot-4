package com.learningspringboot4;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/videos")
public class VideoReflectionController {

    @PostMapping("/reflect")
    public String reflect(@RequestBody VideoEntity video) throws Exception {
        Class<?> clazz = video.getClass();

        System.out.println("=== Reflection Demo ===");

        System.out.println("Constructors:");
        for (var constructor : clazz.getDeclaredConstructors()) {
            System.out.println(constructor);
        }

        System.out.println("\nMethods:");
        for (var method : clazz.getDeclaredMethods()) {
            System.out.println(method.getName());
        }

        System.out.println("\nFields:");
        for (var field : clazz.getDeclaredFields()) {
            System.out.println(field.getName());
        }

        return "Reflection executed for: " + video.getName();
    }
}