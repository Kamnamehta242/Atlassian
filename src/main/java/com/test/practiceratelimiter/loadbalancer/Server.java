package com.test.practiceratelimiter.loadbalancer;

import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private final String id;
    private final AtomicInteger activeConnections;
    private final AtomicInteger totalRequestsHandled;

    public Server(String id) {
        this.id = id;
        this.activeConnections = new AtomicInteger(0);
        this.totalRequestsHandled = new AtomicInteger(0);
    }

    public String getId() {
        return id;
    }

    public int getTotalRequestsHandled() {
        return totalRequestsHandled.get();  // Getter for total requests handled
    }

    public int getActiveConnections() {
        return activeConnections.get();
    }

    public void incrementConnections() {
        activeConnections.incrementAndGet();
        totalRequestsHandled.incrementAndGet();
    }

    public void decrementConnections() {
        activeConnections.decrementAndGet();
    }

    public void handleRequest(String request) {
        // Simulate handling a request
        System.out.println("Server " + id + " is handling request: " + request);
        incrementConnections();

        // Simulate some work
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        decrementConnections();
    }
}
