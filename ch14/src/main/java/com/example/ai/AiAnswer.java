package com.example.ai;

//public record AiAnswer(String text, long inputTokens, long outputTokens) {}
public record AiAnswer(
        String title,
        String explanation,
        String example
) {}