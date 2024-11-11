package com.tanji.mailapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.tanji")
public class MailApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailApiApplication.class, args);
	}

}
