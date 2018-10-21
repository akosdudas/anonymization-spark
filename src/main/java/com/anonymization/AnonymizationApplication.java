package com.anonymization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.anonymization.controllers","com.anonymization.services","com.anonymization.configuration"})
@SpringBootApplication
public class AnonymizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnonymizationApplication.class, args);
    }
}
