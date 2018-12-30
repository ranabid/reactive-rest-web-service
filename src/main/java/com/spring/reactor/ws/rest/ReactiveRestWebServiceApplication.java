package com.spring.reactor.ws.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.spring.reactor.ws.rest.common.ApplicationProperties;

@SpringBootApplication
public class ReactiveRestWebServiceApplication implements CommandLineRunner {
	public static Logger LOGGER = LoggerFactory.getLogger(ReactiveRestWebServiceApplication.class);
	@Autowired
	public ApplicationProperties prop;
	public static void main(String[] args) {
		SpringApplication.run(ReactiveRestWebServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		LOGGER.info("App Name: "+prop.getIdmsAuthEndpoints().getGenerateUri());
		
	}
}



