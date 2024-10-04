package com.test.practiceratelimiter.service;

import com.test.practiceratelimiter.utils.RateLimiter;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

    private final RateLimiter rateLimiter;

    public RateLimitService() {
        // Set max 5 tokens, refills 5 tokens every second for each user
        this.rateLimiter = new RateLimiter(5, 5);
    }

    public boolean isRequestAllowed(String userId) {
        return rateLimiter.tryAcquire("1");
    }

}
