package com.example.ai.controller;

import com.example.ai.tools.DateTimeTools;
import com.example.ai.tools.ProductTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AssistantController {

    private final ChatClient chatClient;
    private final DateTimeTools dateTimeTools;
    private final ProductTools productTools;

    public AssistantController(ChatClient chatClient,
                               DateTimeTools dateTimeTools,
                               ProductTools productTools) {
        this.chatClient = chatClient;
        this.dateTimeTools = dateTimeTools;
        this.productTools = productTools;
    }

    record AssistantAnswer(String reply) {}

    // Single tool — date/time only
    @GetMapping("/assistant")
    public AssistantAnswer assist(@RequestParam String question) {
        String reply = chatClient.prompt()
                .user(question)
                .tools(dateTimeTools)
                .call()
                .content();
        return new AssistantAnswer(reply);
    }

    // Multi-tool — date/time + product prices
    @GetMapping("/product-assistant")
    public AssistantAnswer productAssist(@RequestParam String question) {
        String reply = chatClient.prompt()
                .user(question)
                .tools(dateTimeTools, productTools)
                .call()
                .content();
        return new AssistantAnswer(reply);
    }
}
