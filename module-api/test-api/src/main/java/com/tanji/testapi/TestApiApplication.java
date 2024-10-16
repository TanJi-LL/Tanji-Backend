package com.tanji.testapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.tanji")
public class TestApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApiApplication.class, args);
    }

}
