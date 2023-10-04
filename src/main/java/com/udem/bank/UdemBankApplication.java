package com.udem.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class UdemBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(UdemBankApplication.class, args);
	}

}
