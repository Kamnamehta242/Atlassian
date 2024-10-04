package com.test.practiceratelimiter.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class LeakyBucketDemo {
    public static void main(String[] args) {
        // Create a LeakyBucket with a capacity of 5 requests and a leak rate of 1 request per second
        LeakyBucket leakyBucket = new LeakyBucket(5, 1);
        
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // Simulating multiple requests
        for (int i = 0; i < 20; i++) {
            final int requestNumber = i;
            executorService.submit(() -> {
                if (leakyBucket.tryAcquire()) {
                    System.out.println("Request " + requestNumber + " accepted.");
                } else {
                    System.out.println("Request " + requestNumber + " rejected.");
                }
            });

            // Wait for some time before sending the next request
            try {
                Thread.sleep(200); // Simulate the time between requests
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        executorService.shutdown();
    }
}
