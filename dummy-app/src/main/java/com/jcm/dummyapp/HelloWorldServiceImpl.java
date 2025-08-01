package com.jcm.dummyapp;

import org.springframework.stereotype.Service;

@Service
public class HelloWorldServiceImpl implements HelloWorldService {

    @Override
    public String getHelloWorld() {
        return "Hello, World!";
    }
}
