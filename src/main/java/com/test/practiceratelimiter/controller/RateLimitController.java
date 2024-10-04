package com.test.practiceratelimiter.controller;

import com.test.practiceratelimiter.service.RateLimitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimitController {
    private final RateLimitService rateLimitService;

    public RateLimitController(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

//http://localhost:8080/check-rate-limit?userId=user1
    @GetMapping("/check-rate-limit")
    public String checkRateLimit(@RequestParam String userId) {
        if (rateLimitService.isRequestAllowed(userId)) {
            return "Request is allowed for user: " + userId;
        } else {
            return "Rate limit exceeded for user: " + userId;
        }
    }
}
