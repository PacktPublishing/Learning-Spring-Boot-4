package com.example.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
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

    // Heading 3 — provides conversation memory for the RAG chatbot endpoints.
    // Replace with a JDBC-backed implementation in production so history
    // survives application restarts.
    @Bean
    InMemoryChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }
}
