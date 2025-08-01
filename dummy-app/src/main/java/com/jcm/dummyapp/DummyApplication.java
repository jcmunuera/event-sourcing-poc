package com.jcm.dummyapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jcm.dummyapp", "com.jcm.dummyapp.config", "com.jcm.common.context"})
public class DummyApplication {

    public static void main(String[] args) {
        SpringApplication.run(DummyApplication.class, args);
    }
}
