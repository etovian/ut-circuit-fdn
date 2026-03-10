package com.utcfdn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UtCircuitFdnApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtCircuitFdnApplication.class, args);
	}

}
