package com.example.springbootdocker;

import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class SpringbootdockerApplication {

	static Logger logger= (Logger) LoggerFactory.getLogger(SpringbootdockerApplication.class);


	@PostConstruct
	public void init(){
		logger.info("Application Started");
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootdockerApplication.class, args);

		logger.info("Application Executing...");
	}



}
