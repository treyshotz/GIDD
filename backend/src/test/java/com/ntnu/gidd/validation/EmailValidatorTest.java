package com.ntnu.gidd.validation;

import com.ntnu.gidd.dto.UserRegistrationDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public class EmailValidatorTest {

      /**
       * Test that you can create a user with valid input
       * @throws Exception
       */
      @Test
      public void testCreateUserWithValidInput() throws Exception {
            EmailValidator validator = new EmailValidator();
      }
}
