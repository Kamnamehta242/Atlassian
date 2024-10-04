package com.test.practiceratelimiter.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class LeakyBucketTest {
    private LeakyBucket leakyBucket;

    @BeforeEach
    public void setUp() {
        // Initialize the LeakyBucket with a capacity of 5 and a leak rate of 1 request per second
        leakyBucket = new LeakyBucket(5, 1);
    }

    @Test
    public void testInitialState() {
        // Initially, the current level should be 0
        assertEquals(0, leakyBucket.getCurrentLevel());
    }

    @Test
    public void testAcquiringRequests() {
        // Try to acquire 5 requests, all should succeed
        for (int i = 0; i < 5; i++) {
            assertTrue(leakyBucket.tryAcquire());
        }
        // Current level should be 5 after 5 successful acquisitions
        assertEquals(5, leakyBucket.getCurrentLevel());
    }

    @Test
    public void testExceedingCapacity() {
        // Fill the bucket to its capacity
        for (int i = 0; i < 5; i++) {
            assertTrue(leakyBucket.tryAcquire());
        }
        // The next request should fail
        assertFalse(leakyBucket.tryAcquire());
        // Current level should still be 5
        assertEquals(5, leakyBucket.getCurrentLevel());
    }

    @Test
    public void testLeakageOverTime() throws InterruptedException {
        // Fill the bucket to its capacity
        for (int i = 0; i < 5; i++) {
            assertTrue(leakyBucket.tryAcquire());
        }
        assertEquals(5, leakyBucket.getCurrentLevel());

        // Wait for 2 seconds to allow the bucket to leak
        TimeUnit.SECONDS.sleep(2);

        // Check that some requests have leaked; the current level should be 3
        assertEquals(3, leakyBucket.getCurrentLevel());
    }

    @Test
    public void testBucketUnderLeakage() throws InterruptedException {
        // Fill the bucket to its capacity
        for (int i = 0; i < 5; i++) {
            assertTrue(leakyBucket.tryAcquire());
        }
        assertEquals(5, leakyBucket.getCurrentLevel());

        // Wait for 6 seconds to allow the bucket to leak
        TimeUnit.SECONDS.sleep(6);

        // After 6 seconds, the bucket should be empty (level = 0)
        assertEquals(0, leakyBucket.getCurrentLevel());

        // New requests should be accepted now
        assertTrue(leakyBucket.tryAcquire());
        assertEquals(1, leakyBucket.getCurrentLevel());
    }
}
