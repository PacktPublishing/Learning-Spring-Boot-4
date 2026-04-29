package com.example.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("""
                        You are a helpful technical assistant for TechStore.
                        Keep answers focused and accurate.
                        """)
                .build();
    }

    // Heading 3 — conversation memory for the RAG chatbot endpoints.
    // In Spring AI 2.0.0-M5, InMemoryChatMemory was replaced by
    // MessageWindowChatMemory backed by InMemoryChatMemoryRepository.
    // Replace with a JDBC-backed repository in production.
    @Bean
    MessageWindowChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();
    }
}
