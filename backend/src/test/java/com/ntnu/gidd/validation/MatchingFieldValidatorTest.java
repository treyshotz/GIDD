package com.ntnu.gidd.validation;

import com.ntnu.gidd.dto.User.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MatchingFieldValidatorTest {

      private Validator validator;

      @BeforeEach
      public void setUp() {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
      }

      /**
       * Ensures that an error occurs when two fields is unequal
       */
      @Test
      public void testNonMatchingFields() {
            UserRegistrationDto user = new UserRegistrationDto("Test", "Testesen", "password", "differentPassword", "test@mail.no", LocalDate.now());

            Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(user);
            assertEquals(violations.size(), 1);
      }

      /**
       * Ensures that there isn't any errors when two fields are the same
       */
      @Test
      public void testMatchingFields() {
            UserRegistrationDto user = new UserRegistrationDto("A", "B", "password", "password", "test@mail.no", LocalDate.now());

            Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(user);
            assertTrue(violations.isEmpty());
      }

}
