package com.example.ai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

// The "test" profile disables the CommandLineRunner smoke test so no real API
// call is made during context loading. A placeholder key is used to satisfy
// the OpenAI starter's required api-key property.
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.ai.openai.api-key=test-key")
class Chapter14DemoApplicationTests {

    @Test
    void contextLoads() {
    }
}
