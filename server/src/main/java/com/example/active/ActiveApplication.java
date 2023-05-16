package com.example.active;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

@SpringBootApplication
public class ActiveApplication {
	public static void main(String[] args) {
		Locale.setDefault(Locale.CANADA);
		SpringApplication.run(ActiveApplication.class, args);
	}
}
