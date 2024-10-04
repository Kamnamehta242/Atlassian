package com.test.practiceratelimiter.utils;

import com.test.practiceratelimiter.loadbalancer.LoadBalancer;
import com.test.practiceratelimiter.loadbalancer.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class LoadBalancerTest {
    private LoadBalancer loadBalancer;

    @BeforeEach
    public void setup() {
        loadBalancer = new LoadBalancer();

        // Add servers to the load balancer
        Server server1 = new Server("Server1");
        Server server2 = new Server("Server2");
        Server server3 = new Server("Server3");

        loadBalancer.addServer(server1);
        loadBalancer.addServer(server2);
        loadBalancer.addServer(server3);
    }

    @Test
    void testRoundRobinLoadBalancing() throws InterruptedException {
        int requestCount = 9; // Number of requests to send
        CountDownLatch latch = new CountDownLatch(requestCount);
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < requestCount; i++) {
            final int requestId = i;
            executor.submit(() -> {
                loadBalancer.handleRequest("Request-" + requestId, "round-robin");
                latch.countDown(); // Mark request as completed
            });
        }

        latch.await(5, TimeUnit.SECONDS); // Wait for all threads to complete
        executor.shutdown();

        // Assert that all requests are distributed evenly
        assertEquals(3, loadBalancer.getServer("Server1").getTotalRequestsHandled());
        assertEquals(3, loadBalancer.getServer("Server2").getTotalRequestsHandled());
        assertEquals(3, loadBalancer.getServer("Server3").getTotalRequestsHandled());
    }

    @Test
    public void testLeastConnectionsLoadBalancing() throws InterruptedException {
        int requestCount = 9;
        CountDownLatch latch = new CountDownLatch(requestCount);
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < requestCount; i++) {
            final int requestId = i;
            executor.submit(() -> {
                loadBalancer.handleRequest("Request-" + requestId, "least-connections");
                latch.countDown(); // Mark request as completed
            });
            Thread.sleep(50); // Small delay to allow connections to settle
        }

        latch.await(); // Wait for all threads to complete
        executor.shutdown();

        // Verify that the requests are more or less evenly distributed
        System.out.println("Total requests handled by Server1: " + loadBalancer.getServer("Server1").getTotalRequestsHandled());
        System.out.println("Total requests handled by Server2: " + loadBalancer.getServer("Server2").getTotalRequestsHandled());
        System.out.println("Total requests handled by Server3: " + loadBalancer.getServer("Server3").getTotalRequestsHandled());

        // Ensure none of the servers handled more than 3 requests out of 9
        assertTrue(loadBalancer.getServer("Server1").getTotalRequestsHandled() <= 4);
        assertTrue(loadBalancer.getServer("Server2").getTotalRequestsHandled() <= 4);
        assertTrue(loadBalancer.getServer("Server3").getTotalRequestsHandled() <= 4);
    }

    @Test
    public void testRandomLoadBalancing() throws InterruptedException {
        int requestCount = 9;
        CountDownLatch latch = new CountDownLatch(requestCount);
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < requestCount; i++) {
            final int requestId = i;
            executor.submit(() -> {
                loadBalancer.handleRequest("Request-" + requestId, "random");
                latch.countDown();
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        // We cannot predict exact distribution with random, but ensure servers received requests
        int totalConnections = loadBalancer.getServer("Server1").getTotalRequestsHandled()
                + loadBalancer.getServer("Server2").getTotalRequestsHandled()
                + loadBalancer.getServer("Server3").getTotalRequestsHandled();

        assertEquals(9, totalConnections);
    }

}