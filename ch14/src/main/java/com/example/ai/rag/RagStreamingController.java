package com.example.ai.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/ai")
public class RagStreamingController {

    private final ChatClient         chatClient;
    private final VectorStore        vectorStore;
    private final MessageWindowChatMemory chatMemory;

    public RagStreamingController(ChatClient chatClient,
                                   VectorStore vectorStore,
                                   MessageWindowChatMemory chatMemory) {
        this.chatClient  = chatClient;
        this.vectorStore = vectorStore;
        this.chatMemory  = chatMemory;
    }

    // Streaming RAG chatbot (Heading 4).
    // In Spring AI 2.0.0-M5:
    //   - InMemoryChatMemory replaced by MessageWindowChatMemory
    //   - QuestionAnswerAdvisor replaced by RetrievalAugmentationAdvisor (spring-ai-rag)
    @GetMapping(value = "/chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(
            @RequestParam String message,
            @RequestParam(defaultValue = "default-session") String conversationId) {

        return chatClient.prompt()
                .user(message)
                .advisors(advisor -> advisor
                        .advisors(
                            new SimpleLoggerAdvisor(),
                            MessageChatMemoryAdvisor.builder(chatMemory).build(),
                            RetrievalAugmentationAdvisor.builder()
                                    .documentRetriever(VectorStoreDocumentRetriever.builder()
                                            .vectorStore(vectorStore)
                                            .topK(4)
                                            .build())
                                    .build()
                        )
                        .param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }
}
