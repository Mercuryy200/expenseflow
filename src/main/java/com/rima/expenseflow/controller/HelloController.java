package com.rima.expenseflow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Spring Boot!";
    }

    @GetMapping("/status")
    public Status getStatus() {
        return new Status("ExpenseFlow API", "1.0.0", "Running");
    }

    record Status(String name, String version, String status) {}
}