//package com.example.ai.controller;
//
//import com.example.ai.service.CodeReviewService;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/ai")
//public class CodeReviewController {
//
//    private final CodeReviewService codeReviewService;
//
//    public CodeReviewController(CodeReviewService codeReviewService) {
//        this.codeReviewService = codeReviewService;
//    }
//
//    record ReviewRequest(String language, String code) {}
//    record ReviewResult(String feedback) {}
//
//    @PostMapping("/review")
//    public ReviewResult review(@RequestBody ReviewRequest request) {
//        return new ReviewResult(
//                codeReviewService.review(request.language(), request.code())
//        );
//    }
//}
