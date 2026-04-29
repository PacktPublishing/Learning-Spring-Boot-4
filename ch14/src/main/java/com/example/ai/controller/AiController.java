package com.example.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final ChatClient chatClient;

    public AiController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    // ── Heading 1 ────────────────────────────────────────────────────────────

    record AiAnswer(String text, long inputTokens, long outputTokens) {}

    @GetMapping("/ask")
    public AiAnswer ask(@RequestParam String question) {
        ChatResponse response = chatClient.prompt()
                .user(question)
                .call()
                .chatResponse();

        return new AiAnswer(
                response.getResult().getOutput().getText(),
                response.getMetadata().getUsage().getPromptTokens(),
                response.getMetadata().getUsage().getGenerationTokens()
        );
    }

    // ── Heading 2 — Structured Outputs ───────────────────────────────────────

    record BookSummary(String title, String author, String oneLiner) {}

    @GetMapping("/summary")
    public BookSummary summarize(@RequestParam String book) {
        return chatClient.prompt()
                .user("Describe the book '" + book + "' with title, author, and a one-line summary.")
                .call()
                .entity(BookSummary.class);
    }
}
