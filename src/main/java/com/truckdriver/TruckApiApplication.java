package com.truckdriver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "sharedEntity")
@EnableJpaRepositories(basePackages = "sharedDao")
public class TruckApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TruckApiApplication.class, args);
	}

}
