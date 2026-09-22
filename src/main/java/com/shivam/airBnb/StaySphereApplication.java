package com.shivam.airBnb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StaySphereApplication {

	public static void main(String[] args) {
		SpringApplication.run(StaySphereApplication.class, args);
	}

}
