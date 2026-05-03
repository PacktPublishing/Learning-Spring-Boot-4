package com.example.ai.controller;

import com.example.ai.AiAnswer;
import com.example.ai.BookSummary;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class AiController {

    private final ChatClient chatClient;

    public AiController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/api/ai/text-response/java-assistant/ask")
    public String askReturnText(@RequestParam String question) {
        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }

    @GetMapping("/api/ai/chat-response/java-assistant/ask")
    public ChatResponse askReturnChatResponse(@RequestParam String question) {
        ChatResponse response = chatClient.prompt()
                .user(question)
                .call()
                .chatResponse();
        return response;
    }

    @GetMapping("/api/ai/structured-response/java-assistant/ask")
    public AiAnswer askStructureResponse(@RequestParam String question) {
        return chatClient.prompt()
                .system("""
                        You are a Java and Spring Boot expert.
                        Answer the question and return the result as JSON with the following fields:
                        - title
                        - explanation
                        - example
                        """)
                .user(question)
                .call()
                .entity(AiAnswer.class);
    }

    @GetMapping("/api/ai/book/summary")
    public BookSummary summarize(@RequestParam String book) {
        return chatClient.prompt()
                .user("Describe the book '" + book + "' with title, author, and a description.")
                .call()
                .entity(BookSummary.class);
    }

    @GetMapping("/api/ai/book/summary-list")
    public List<BookSummary> summarizeList(@RequestParam String book) {
        return chatClient.prompt()
                .user("List three classic Java books with title, author, and a description.")
                .call()
                .entity(new ParameterizedTypeReference<List<BookSummary>>() {});
    }





//    public AiAnswer askReturnStructuredResponse(@RequestParam String question) {
//        ChatResponse response = chatClient.prompt()
//                .user(question)
//                .call()
//                .chatResponse();
//
//        return new AiAnswer(
//                response.getResult().getOutput().getText(),
//                response.getMetadata().getUsage().getPromptTokens(),
//                response.getMetadata().getUsage().getTotalTokens()
//        );
//
//
//    }
}