package com.example.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class RagChatbotController {

    private final ChatClient           chatClient;
    private final VectorStore          vectorStore;
    private final MessageWindowChatMemory chatMemory;

    public RagChatbotController(ChatClient chatClient,
                                VectorStore vectorStore,
                                MessageWindowChatMemory chatMemory) {
        this.chatClient  = chatClient;
        this.vectorStore = vectorStore;
        this.chatMemory  = chatMemory;
    }

    record ChatbotAnswer(String reply, String conversationId) {}

    @GetMapping("/chat")
    public ChatbotAnswer chat(
            @RequestParam String message,
            @RequestParam(defaultValue = "default-session") String conversationId) {

        String reply = chatClient.prompt()
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
                .call()
                .content();

        return new ChatbotAnswer(reply, conversationId);
    }
}