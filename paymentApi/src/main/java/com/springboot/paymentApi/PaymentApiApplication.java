package com.springboot.paymentApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class PaymentApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentApiApplication.class, args);
	}

	/*protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PaymentApiApplication.class);
	}*/

}
