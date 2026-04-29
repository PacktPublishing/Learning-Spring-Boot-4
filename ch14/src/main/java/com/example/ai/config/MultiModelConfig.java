package com.example.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Multi-provider configuration — demonstrates using two LLMs simultaneously.
 *
 * To activate:
 *   1. Uncomment spring-ai-starter-model-ollama in pom.xml
 *   2. Uncomment the Ollama properties in application.properties
 *   3. Run `ollama serve` locally
 *   4. Start the app with: --spring.profiles.active=multi-model
 *
 * When active, two named ChatClient beans are created: "openai" and "local".
 * Inject them with @Qualifier("openai") or @Qualifier("local").
 */
@Configuration
@Profile("multi-model")
public class MultiModelConfig {

    @Bean
    @Qualifier("openai")
    ChatClient openAiClient(
            @Qualifier("openAiChatClientBuilder") ChatClient.Builder builder) {
        return builder
                .defaultSystem("You are a reasoning expert. Think step by step.")
                .build();
    }

    @Bean
    @Qualifier("local")
    ChatClient ollamaClient(
            @Qualifier("ollamaChatClientBuilder") ChatClient.Builder builder) {
        return builder
                .defaultSystem("You are a fast assistant. Be brief.")
                .build();
    }
}
