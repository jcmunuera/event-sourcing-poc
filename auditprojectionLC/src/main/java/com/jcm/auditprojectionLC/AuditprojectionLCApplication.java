package com.jcm.auditprojectionLC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class AuditprojectionLCApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuditprojectionLCApplication.class, args);
    }
}