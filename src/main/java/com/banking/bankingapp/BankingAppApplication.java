package com.banking.bankingapp;

import org.apache.logging.log4j.Level;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class BankingAppApplication {
    protected static final Logger logger = LogManager.getLogger();

	public static void main(String[] args) {
        logger.info("Hello world!");
		logger.log(Level.INFO, "Hiii");
		SpringApplication.run(BankingAppApplication.class, args);

	}

}
