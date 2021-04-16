package com.ntnu.gidd.validation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.apache.commons.validator.routines.EmailValidator;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public class EmailValidatorTest {
	
	@Test
	public void testWithValidEmail() {
		EmailValidator validator = EmailValidator.getInstance();
		List<String> messages = Arrays.asList("test@mail.no", "test@mail.no.in", "test.testesen@mail.no", "test#test@mail.no");
		messages.forEach(message -> {
			assertTrue(validator.isValid(message));
		});
	}
	
	@Test
	public void testWithInvalidEmail() {
		EmailValidator validator = EmailValidator.getInstance();
		List<String> messages = Arrays.asList("testmail.no" , "test@test", "test.no", "@test");
		messages.forEach(message -> {
			assertFalse(validator.isValid(message));
		});
	}
}
