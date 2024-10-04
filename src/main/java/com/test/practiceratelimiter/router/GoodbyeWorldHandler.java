package com.test.practiceratelimiter.router;

public class GoodbyeWorldHandler implements RouteHandler {
    @Override
    public void handle(String path) {
        System.out.println("Goodbye, World! You accessed: " + path);
    }
}