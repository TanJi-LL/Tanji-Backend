package com.tanji.domainredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.tanji")
public class DomainRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(DomainRedisApplication.class, args);
    }

}
