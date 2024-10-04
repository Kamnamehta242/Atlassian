package com.test.practiceratelimiter.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final List<String> users = new ArrayList<>();

    @GetMapping
    public List<String> getAllUsers() {
        return users;
    }

    @PostMapping
    public String createUser(@RequestBody String user) {
        users.add(user);
        return "User created: " + user;
    }
}
