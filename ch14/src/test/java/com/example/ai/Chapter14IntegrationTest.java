package com.example.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Heading 5 — Testcontainers integration test.
 *
 * Starts a real pgvector/pgvector:pg17 Docker container automatically.
 * @ServiceConnection auto-configures the datasource URL, username, and password
 * from the running container — no manual property overrides needed.
 *
 * Requires:
 *   - Docker running on the host machine
 *   - OPENAI_API_KEY env var set (for embeddings and evaluation)
 *
 * The "test" profile suppresses DocumentIngestionService's @PostConstruct,
 * so @BeforeEach manually ingests the product FAQ before each test.
 */
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.ai.openai.api-key=${OPENAI_API_KEY:#{null}}",
        "spring.ai.vectorstore.pgvector.initialize-schema=true"
})
class Chapter14IntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("pgvector/pgvector:pg17");

    @Autowired
    ChatModel chatModel;

    @Autowired
    VectorStore vectorStore;

    @Value("classpath:documents/product-faq.txt")
    Resource faqResource;

    @BeforeEach
    void ingestDocuments() {
        // DocumentIngestionService is disabled in the "test" profile,
        // so we ingest documents manually before each test.
        TextReader reader = new TextReader(faqResource);
        reader.getCustomMetadata().put("source", "product-faq.txt");

        List<Document> chunks = TokenTextSplitter.builder()
                .withChunkSize(800)
                .withMinChunkSizeChars(100)
                .build()
                .apply(reader.read());

        vectorStore.accept(chunks);
    }

    @Test
    void ragResponseIsRelevantAndGrounded() {
        String question = "What shipping options does TechStore offer?";

        ChatResponse ragResponse = ChatClient.builder(chatModel).build()
                .prompt(question)
                .advisors(RetrievalAugmentationAdvisor.builder()
                        .documentRetriever(VectorStoreDocumentRetriever.builder()
                                .vectorStore(vectorStore)
                                .topK(4)
                                .build())
                        .build())
                .call()
                .chatResponse();

        String answer = ragResponse.getResult().getOutput().getText();
        assertThat(answer).isNotBlank();

        @SuppressWarnings("unchecked")
        List<Document> context = (List<Document>) ragResponse.getMetadata()
                .get(RetrievalAugmentationAdvisor.DOCUMENT_CONTEXT);

        EvaluationResponse verdict = new RelevancyEvaluator(ChatClient.builder(chatModel))
                .evaluate(new EvaluationRequest(question, context, answer));

        assertThat(verdict.isPass())
                .as("RAG response must be grounded in the retrieved documents")
                .isTrue();
    }
}
