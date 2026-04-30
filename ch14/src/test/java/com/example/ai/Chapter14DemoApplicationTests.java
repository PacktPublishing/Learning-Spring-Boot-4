package com.example.ai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

// The "test" profile disables:
//   - CommandLineRunner smoke test (no real API call on startup)
//   - DocumentIngestionService @PostConstruct (no vector store needed)
// @MockitoBean VectorStore prevents pgvector from connecting to a real PostgreSQL.
// A placeholder key satisfies the OpenAI starter's required api-key property.
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.ai.openai.api-key=test-key")
class Chapter14DemoApplicationTests {

    @MockitoBean
    VectorStore vectorStore;

    @Test
    void contextLoads() {
    }
}
