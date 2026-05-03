package com.example.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/ai")
public class StreamingController {

    private final ChatClient chatClient;

    public StreamingController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping(value = "/api/ai/text-response-flux/java-assistant/ask", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> askReturnTextFlux(@RequestParam String question) {
        return chatClient.prompt()
                .user(question)
                .stream()
                .content();
    }
}
