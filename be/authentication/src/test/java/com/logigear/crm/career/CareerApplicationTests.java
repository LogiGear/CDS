package com.logigear.crm.career;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CareerApplicationTests {

	@BeforeAll
	static void setup() {
		System.err.println("Executes once before all test methods in this class");
	}

	@BeforeEach
	void init() {
		System.err.println("Executes before each test method in this class");
	}

	@Test
	void contextLoads() {
	}

}
