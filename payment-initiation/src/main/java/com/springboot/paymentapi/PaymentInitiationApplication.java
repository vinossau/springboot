package com.springboot.paymentapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class PaymentInitiationApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentInitiationApplication.class, args);
	}


}
