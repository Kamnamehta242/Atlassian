package com.test.practiceratelimiter.utils;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class SlidingWindowRateLimiter {
    private final long timeWindowMillis;
    private final int maxRequests;
    private final ConcurrentLinkedQueue<Long> requestTimestamps;

    public SlidingWindowRateLimiter(int maxRequests, long timeWindow, TimeUnit timeUnit) {
        this.maxRequests = maxRequests;
        this.timeWindowMillis = timeUnit.toMillis(timeWindow);
        this.requestTimestamps = new ConcurrentLinkedQueue<>();
    }

    public synchronized boolean tryAcquire() {
        long currentTime = System.currentTimeMillis();
        
        // Remove timestamps that are outside the time window
        while (!requestTimestamps.isEmpty() && 
               (currentTime - requestTimestamps.peek() > timeWindowMillis)) {
            requestTimestamps.poll();
        }

        // Check if we can allow the request
        if (requestTimestamps.size() < maxRequests) {
            requestTimestamps.offer(currentTime);
            return true; // Request is allowed
        } else {
            return false; // Request is rejected
        }
    }
}
