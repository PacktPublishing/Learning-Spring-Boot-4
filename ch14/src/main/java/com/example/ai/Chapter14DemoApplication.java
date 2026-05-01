package com.example.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Chapter14DemoApplication {

    static void main(String[] args) {
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
