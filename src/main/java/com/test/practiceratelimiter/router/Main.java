package com.test.practiceratelimiter.router;

public class Main {
    public static void main(String[] args) {
        Router router = new Router();

        // Register routes with their corresponding handlers
        router.registerRoute("/hello", new HelloWorldHandler());
        router.registerRoute("/goodbye", new GoodbyeWorldHandler());

        // Simulate some requests
        String[] requests = {"/hello", "/goodbye", "/unknown"};

        for (String request : requests) {
            System.out.println("Requesting: " + request);
            router.route(request);
            System.out.println();
        }
    }
}
