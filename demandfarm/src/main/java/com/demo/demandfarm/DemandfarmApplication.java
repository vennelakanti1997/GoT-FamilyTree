package com.demo.demandfarm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.demo.demandfarm.*"})
public class DemandfarmApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemandfarmApplication.class, args);
	}

}
