package com.test.practiceratelimiter.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class RateLimiter {
    private final long maxTokens;       // Max tokens in the bucket
    private final long refillRate;      // Tokens added per second
    private final Map<String, UserBucket> userBuckets = new ConcurrentHashMap<>(); // Store token buckets for each user

    public RateLimiter(long maxTokens, long refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
    }

    public synchronized boolean tryAcquire(String userId) {
        UserBucket userBucket = userBuckets.computeIfAbsent(userId, id -> new UserBucket(maxTokens, refillRate));
        return userBucket.tryAcquire();
    }

    // Inner class to represent each user's bucket
    private static class UserBucket {
        private double availableTokens;
        private long lastRefillTimestamp;
        private final long refillRate;
        private final long maxTokens;

        public UserBucket(long maxTokens, long refillRate) {
            this.maxTokens = maxTokens;
            this.refillRate = refillRate;
            this.availableTokens = maxTokens;
            this.lastRefillTimestamp = System.nanoTime();
        }

        public boolean tryAcquire() {
            refillTokens();
            if (availableTokens >= 1) {
                availableTokens--;
                return true;
            }
            return false;
        }

        private void refillTokens() {
            long now = System.nanoTime();
            long timeSinceLastRefill = now - lastRefillTimestamp;
            long tokensToAdd = timeSinceLastRefill * refillRate / TimeUnit.SECONDS.toNanos(1);
            if (tokensToAdd > 0) {
                availableTokens = Math.min(maxTokens, availableTokens + tokensToAdd);
                lastRefillTimestamp = now;
            }
        }
    }
}
