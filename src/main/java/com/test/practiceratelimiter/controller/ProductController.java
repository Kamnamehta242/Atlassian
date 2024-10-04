package com.test.practiceratelimiter.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final List<String> products = new ArrayList<>();

    @GetMapping
    public List<String> getAllProducts() {
        return products;
    }

    @PostMapping
    public String createProduct(@RequestBody String product) {
        products.add(product);
        return "Product created: " + product;
    }
}
