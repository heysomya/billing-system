package com.billingSystem.stock_management_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockManagementServiceApplication.class, args);
	}

}
