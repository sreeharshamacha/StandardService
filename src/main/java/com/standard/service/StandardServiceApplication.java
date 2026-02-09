package com.standard.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class StandardServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StandardServiceApplication.class, args);
    }
}
