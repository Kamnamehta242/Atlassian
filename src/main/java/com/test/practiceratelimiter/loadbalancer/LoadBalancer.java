package com.test.practiceratelimiter.loadbalancer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancer {
    private final List<Server> servers = new ArrayList<>();
    private AtomicInteger roundRobinIndex = new AtomicInteger(0);

    public void addServer(Server server) {
        servers.add(server);
    }

    // Round-robin strategy
    public void handleRequest(String request, String strategy) {
        Server selectedServer = null;

        switch (strategy) {
            case "round-robin":
                selectedServer = getNextServerRoundRobin();
                break;
            case "least-connections":
                selectedServer = getLeastConnectionsServer();
                break;
            case "random":
                selectedServer = getRandomServer();
                break;
        }

        if (selectedServer != null) {
            selectedServer.handleRequest(request);
        }
    }

    private Server getNextServerRoundRobin() {
        int index = roundRobinIndex.getAndIncrement() % servers.size();
        return servers.get(index);
    }

    private Server getLeastConnectionsServer() {
        // Log current state of connections before choosing the least-connected server
        servers.forEach(server ->
                System.out.println("Server " + server.getId() + " has " + server.getActiveConnections() + " active connections.")
        );

        // Select the server with the least active connections
        Server leastConnectionServer = servers.stream()
                .min((s1, s2) -> Integer.compare(s1.getActiveConnections(), s2.getActiveConnections()))
                .orElse(null);

        System.out.println("Selected Server: " + leastConnectionServer.getId() + " for next request.");

        return leastConnectionServer;
    }


    private Server getRandomServer() {
        Random random = new Random();
        return servers.get(random.nextInt(servers.size()));
    }

    // Helper method to retrieve a server by its name (for testing purposes)
    public Server getServer(String serverName) {
        return servers.stream()
                .filter(server -> server.getId().equals(serverName))
                .findFirst()
                .orElse(null);
    }
}
