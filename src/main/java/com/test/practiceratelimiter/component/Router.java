//package com.test.practiceratelimiter.component;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class Router {
//    private final Map<String, Map<String, Object>> routes = new HashMap<>();
//
//    @Autowired
//    private UserController userController;
//
//    @Autowired
//    private ProductController productController;
//
//    public Router() {
//        // Define routes
//        addRoute("GET", "/users", userController::getAllUsers);
//        addRoute("GET", "/products", productController::getAllProducts);
//        addRoute("POST", "/users", userController::createUser);
//        addRoute("POST", "/products", productController::createProduct);
//    }
//
//    public void addRoute(String method, String path, Object handler) {
//        routes.computeIfAbsent(path, k -> new HashMap<>()).put(method, handler);
//    }
//
//    public Object route(String method, String path) {
//        Map<String, Object> methodMap = routes.get(path);
//        if (methodMap != null) {
//            return methodMap.get(method);
//        }
//        throw new IllegalArgumentException("No route found for " + method + " " + path);
//    }
//}
