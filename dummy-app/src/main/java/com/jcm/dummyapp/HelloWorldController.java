package com.jcm.dummyapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class HelloWorldController {

    private final HelloWorldService helloWorldService;

    public HelloWorldController(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    @GetMapping("/hello")
    public HelloWorldResponse helloWorld() {
        return new HelloWorldResponse(helloWorldService.getHelloWorld());
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        // Simulate login logic
        System.out.println("Login attempt for user: " + loginRequest.getUsername());
        String sessionId = UUID.randomUUID().toString();
        return new LoginResponse(sessionId);
    }
}

