package com.test.practiceratelimiter.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class RateLimiterTest {

    private RateLimiter rateLimiter;

    @BeforeEach
    void setUp() {
    rateLimiter = new RateLimiter(3, 2);
    }

    @Test
    void testInitialTokenAvailability() {
        assertTrue(rateLimiter.tryAcquire("1"));
        assertTrue(rateLimiter.tryAcquire("1"));
        assertTrue(rateLimiter.tryAcquire("1"));
    }

    @Test
    void testExceedingTokenAvailability() {
        assertTrue(rateLimiter.tryAcquire("1"));
        assertTrue(rateLimiter.tryAcquire("1"));
        assertTrue(rateLimiter.tryAcquire("1"));
        assertFalse(rateLimiter.tryAcquire("1"),"RateLimiter should not allow more than 3 requests per second");
    }

    @Test
    public void testTokenRefill() throws InterruptedException {
        // Consume all tokens
        assertTrue(rateLimiter.tryAcquire("1"));
        assertTrue(rateLimiter.tryAcquire("1"));
        assertTrue(rateLimiter.tryAcquire("1"));

        // No tokens should be available now
        assertFalse(rateLimiter.tryAcquire("1"), "No tokens should be available after consuming all");

        // Wait for 1 second to allow refill
        Thread.sleep(1000);

        // Now a token should be available again
        assertTrue(rateLimiter.tryAcquire("1"), "Token should be available after 1 second");
    }

    @Test
    public void testBurstRequests() throws InterruptedException {
        // Initially, all tokens are available
        assertTrue(rateLimiter.tryAcquire("1"));
        assertTrue(rateLimiter.tryAcquire("1"));
        assertTrue(rateLimiter.tryAcquire("1"));

        // Now, requests should be denied
        assertFalse(rateLimiter.tryAcquire("1"), "Burst limit exceeded");

        // Wait for tokens to refill
        Thread.sleep(1000);

        // Ensure burst requests are handled after refill
        assertTrue(rateLimiter.tryAcquire("1"), "Token should be available after refill");
    }

    @Test
    public void testConcurrency() throws InterruptedException {
        int numberOfThreads = 20; // Simulate 20 concurrent threads
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(1); // To ensure all threads start at the same time
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads); // To wait for all threads to finish

        // Store results of each thread (true if acquired a token, false if not)
        ConcurrentLinkedQueue<Boolean> results = new ConcurrentLinkedQueue<>();

        // Define a task for each thread to try to acquire a token
        Runnable task = () -> {
            try {
                latch.await(); // Wait until the latch is released
                results.add(rateLimiter.tryAcquire("1"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                doneLatch.countDown(); // Mark thread as finished
            }
        };

        // Submit the task for each thread
        IntStream.range(0, numberOfThreads).forEach(i -> executorService.submit(task));

        // Release the latch so all threads start together
        latch.countDown();

        // Wait for all threads to finish
        doneLatch.await();

        // Shutdown the executor service
        executorService.shutdown();

        // Analyze results
        long successCount = results.stream().filter(success -> success).count();
        long failCount = results.stream().filter(success -> !success).count();

        // We expect only up to 10 requests to succeed, because max tokens = 10
        System.out.println("Success count: " + successCount + " | Fail count: " + failCount);
        assertEquals(3, successCount); // Only 10 should succeed
        assertEquals(numberOfThreads - 3, failCount); // The rest should fail
    }
}