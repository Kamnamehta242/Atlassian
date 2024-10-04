package com.test.practiceratelimiter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class RateLimitServiceTest {
    private RateLimitService rateLimitService;

    @BeforeEach
    public void setup() {
        rateLimitService = new RateLimitService();
    }

    @Test
     void testMultipleRequestsAllowed() {
        String userId = "user1";

        // First 5 requests should be allowed
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimitService.isRequestAllowed(userId), "Request should be allowed");
        }

        // 6th request should be blocked
        assertFalse(rateLimitService.isRequestAllowed(userId), "Rate limit should be exceeded");
    }

    @Test
     void testSeparateUsers() {
        String user1 = "user1";
        String user2 = "user2";

        // First 5 requests for both users should be allowed
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimitService.isRequestAllowed(user1), "User1 request should be allowed");
            assertTrue(rateLimitService.isRequestAllowed(user2), "User2 request should be allowed");
        }

        // Both should hit the rate limit after 5 requests
        assertFalse(rateLimitService.isRequestAllowed(user1), "User1 should hit rate limit");
        assertFalse(rateLimitService.isRequestAllowed(user2), "User2 should hit rate limit");
    }

}