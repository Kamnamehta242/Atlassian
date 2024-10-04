package com.test.practiceratelimiter.loadbalancer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadBalancerMain {
    public static void main(String[] args) {
        LoadBalancer loadBalancer = new LoadBalancer();

        // Add backend servers to the load balancer
        Server server1 = new Server("Server1");
        Server server2 = new Server("Server2");
        Server server3 = new Server("Server3");

        loadBalancer.addServer(server1);
        loadBalancer.addServer(server2);
        loadBalancer.addServer(server3);

        // Simulate concurrent requests
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 20; i++) {
            final int requestId = i;
            executor.submit(() -> {
                loadBalancer.handleRequest("Request-" + requestId, "round-robin");
            });
        }

        executor.shutdown();
    }
}

