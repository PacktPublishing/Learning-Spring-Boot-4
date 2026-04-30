package com.example.ai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Heading 5 — LLM-as-a-Judge evaluation test.
 *
 * Requires a REAL OpenAI API key and a running pgvector instance.
 * Run the Docker stack first: docker compose up -d
 * Set env var: export OPENAI_API_KEY=sk-...
 *
 * DocumentIngestionService loads product-faq.txt via @PostConstruct on startup,
 * so the vector store will be populated before the test runs.
 * The @ActiveProfiles("!test") guard on DocumentIngestionService is overridden
 * here by NOT using the "test" profile.
 */
@SpringBootTest
@TestPropertySource(properties = "spring.ai.openai.api-key=${OPENAI_API_KEY:#{null}}")
class RagEvaluationTest {

    @Autowired
    ChatModel chatModel;

    @Autowired
    VectorStore vectorStore;

    @Test
    void ragAnswerShouldBeRelevantToQuestion() {
        String question = "What is the TechStore return policy?";

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

        @SuppressWarnings("unchecked")
        List<Document> context = (List<Document>) ragResponse.getMetadata()
                .get(RetrievalAugmentationAdvisor.DOCUMENT_CONTEXT);

        EvaluationRequest evalRequest = new EvaluationRequest(question, context, answer);

        EvaluationResponse verdict = new RelevancyEvaluator(ChatClient.builder(chatModel))
                .evaluate(evalRequest);

        assertThat(verdict.isPass())
                .as("RAG answer should be relevant to the question and grounded in the context")
                .isTrue();
    }
}
