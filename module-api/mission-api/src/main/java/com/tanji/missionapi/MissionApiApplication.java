package com.tanji.missionapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.tanji")
public class MissionApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MissionApiApplication.class, args);
    }

}
