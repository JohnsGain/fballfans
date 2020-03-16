package com.rabbit.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

@SpringBootConfiguration
@ContextConfiguration
@SpringBootTest
class DemoApplicationTests {


	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
	@Test
	void contextLoads() {
		System.out.println(objectMapper());
	}

}
