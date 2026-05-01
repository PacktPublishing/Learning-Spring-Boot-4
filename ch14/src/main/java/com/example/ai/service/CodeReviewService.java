//package com.example.ai.service;
//
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.prompt.PromptTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@Service
//public class CodeReviewService {
//
//    private final ChatClient chatClient;
//
//    @Value("classpath:prompts/code-review.st")
//    private Resource reviewPromptResource;
//
//    public CodeReviewService(ChatClient chatClient) {
//        this.chatClient = chatClient;
//    }
//
//    public String review(String language, String code) {
//        var template = new PromptTemplate(reviewPromptResource);
//        var prompt = template.create(Map.of(
//                "language", language,
//                "code", code
//        ));
//        return chatClient.prompt(prompt).call().content();
//    }
//}
