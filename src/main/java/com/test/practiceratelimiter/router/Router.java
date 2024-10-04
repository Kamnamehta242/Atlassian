package com.test.practiceratelimiter.router;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private Map<String, RouteHandler> routes;

    public Router() {
        routes = new HashMap<>();
    }

    public void registerRoute(String path, RouteHandler handler) {
        routes.put(path, handler);
    }

    public void route(String path) {
        RouteHandler handler = routes.get(path);
        if (handler != null) {
            handler.handle(path);
        } else {
            System.out.println("404 Not Found: " + path);
        }
    }
}
