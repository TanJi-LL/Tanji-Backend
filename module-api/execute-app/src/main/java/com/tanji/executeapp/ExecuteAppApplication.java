package com.tanji.executeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.tanji")
public class ExecuteAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExecuteAppApplication.class, args);
	}

}
