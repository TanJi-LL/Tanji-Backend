package com.tanji.memberapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.tanji")
public class MemberApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberApiApplication.class, args);
    }

}
