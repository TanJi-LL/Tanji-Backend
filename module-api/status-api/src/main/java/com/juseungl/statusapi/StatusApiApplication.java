package com.juseungl.statusapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.tanji")
public class StatusApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatusApiApplication.class, args);
    }

}
