package com.example.subscription_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class SubscriptionApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionApiApplication.class, args);

		System.out.println("HI");
	}

}
