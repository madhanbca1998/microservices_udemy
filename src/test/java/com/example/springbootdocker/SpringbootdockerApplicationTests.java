package com.example.springbootdocker;

import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SpringbootdockerApplicationTests {


	static Logger logger= (Logger) LoggerFactory.getLogger(SpringbootdockerApplication.class);
	@Test
	void contextLoads() {
		logger.info("Test Cases are executing...");
        assertTrue(true);

	}

}
