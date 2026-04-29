package com.example.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class Chapter14DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(Chapter14DemoApplication.class, args);
    }

    // Smoke test — runs once on startup (skipped during tests via @Profile).
    // Requires OPENAI_API_KEY to be set. Expected output: "Spring AI works."
//    @Bean
//    @Profile("!test")
//    CommandLineRunner smoke(ChatClient.Builder builder) {
//        return args -> {
//            String reply = builder.build()
//                    .prompt()
//                    .user("Reply with exactly three words: Spring AI works.")
//                    .call()
//                    .content();
//            System.out.println(">>> Smoke test: " + reply);
//        };
//    }
}
