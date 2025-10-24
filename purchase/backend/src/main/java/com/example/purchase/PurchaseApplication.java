package com.example.purchase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PurchaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(PurchaseApplication.class, args);
		System.out.println("✅ Purchase Management System - Backend Started Successfully!");
		System.out.println("🔗 API Documentation: http://localhost:8080/swagger-ui.html");
	}
}
