package com.test.practiceratelimiter.utils;

import java.util.concurrent.TimeUnit;

public class LeakyBucket {
    private final long capacity;          // Maximum bucket size
    private final long leakRate;          // Leak rate (requests per second)
    private long currentLevel;             // Current level of the bucket
    private long lastCheckTime;            // Last time the bucket was checked

    public LeakyBucket(long capacity, long leakRate) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.currentLevel = 0;
        this.lastCheckTime = System.nanoTime();
    }

    public synchronized boolean tryAcquire() {
        long now = System.nanoTime();
        long elapsedTime = now - lastCheckTime;

        // Calculate how much water (requests) have leaked in this time
        long leaked = TimeUnit.NANOSECONDS.toSeconds(elapsedTime) * leakRate;
        currentLevel = Math.max(0, currentLevel - leaked); // Ensure it doesn't go below 0
        lastCheckTime = now;

        if (currentLevel < capacity) {
            currentLevel++; // Add the request to the bucket
            return true;    // Request accepted
        }
        return false;       // Request rejected (bucket overflow)
    }

    public synchronized long getCurrentLevel() {
        return currentLevel;
    }
}
