package com.test.practiceratelimiter.router;

public class HelloWorldHandler implements RouteHandler {
    @Override
    public void handle(String path) {
        System.out.println("Hello, World! You accessed: " + path);
    }
}


