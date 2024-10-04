package com.test.practiceratelimiter.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;


public class SlidingWindowRateLimiterTest {
    private SlidingWindowRateLimiter rateLimiter;

    @BeforeEach
    public void setUp() {
        // Initialize the SlidingWindowRateLimiter with a max of 5 requests in a 1-second window
        rateLimiter = new SlidingWindowRateLimiter(5, 1, TimeUnit.SECONDS);
    }

    @Test
    public void testAllowingRequestsWithinLimit() {
        // Try to acquire 5 requests, all should succeed
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimiter.tryAcquire());
        }
        // Current level should be exactly 5, so the next request should fail
        assertFalse(rateLimiter.tryAcquire());
    }

    @Test
    public void testRejectingRequestsOverLimit() {
        // Fill the rate limiter to its maximum
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimiter.tryAcquire());
        }

        // The next request should be rejected
        assertFalse(rateLimiter.tryAcquire());
    }

    @Test
    public void testAllowAfterTimeWindow() throws InterruptedException {
        // Fill the rate limiter to its maximum
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimiter.tryAcquire());
        }

        // The next request should be rejected
        assertFalse(rateLimiter.tryAcquire());

        // Wait for the time window to pass
        TimeUnit.SECONDS.sleep(1);

        // Now, we should be able to acquire a request again
        assertTrue(rateLimiter.tryAcquire());
    }

    @Test
    public void testMultipleBatches() throws InterruptedException {
        // Fill the rate limiter to its maximum
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimiter.tryAcquire());
        }

        // The next request should be rejected
        assertFalse(rateLimiter.tryAcquire());

        // Wait for half the time window
        TimeUnit.MILLISECONDS.sleep(500);

        // Try acquiring two requests, should still be rejected
        assertFalse(rateLimiter.tryAcquire());
        assertFalse(rateLimiter.tryAcquire());

        // Wait for the remaining time window to pass
        TimeUnit.MILLISECONDS.sleep(500);

        // Now, we should be able to acquire 3 requests again (2 from the first batch)
        assertTrue(rateLimiter.tryAcquire());
        assertTrue(rateLimiter.tryAcquire());
        assertTrue(rateLimiter.tryAcquire());

        // The next request should be rejected again
        assertFalse(rateLimiter.tryAcquire());
    }
}
