package com.example.FACTS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FactsApplication {

    private static final Logger log = LoggerFactory.getLogger(FactsApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(FactsApplication.class, args);
	}


}
