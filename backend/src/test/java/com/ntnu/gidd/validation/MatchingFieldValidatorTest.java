package com.ntnu.gidd.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntnu.gidd.controller.UserController;
import com.ntnu.gidd.dto.UserRegistrationDto;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.TrainingLevelRepository;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.service.ActivityServiceImpl;
import com.ntnu.gidd.service.UserServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public class MatchingFieldValidatorTest {

      private Validator validator;

      @BeforeEach
      public void setUp() {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
      }
      @Test
      public void testNonMatchingFields() {
            UserRegistrationDto user = new UserRegistrationDto("Test", "Testesen", "password", "differentPassword", "test@mail.no", LocalDate.now());

            Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(user);
            assertEquals(violations.size(), 1);
      }

      @Test
      public void testMatchingFields() {
            UserRegistrationDto user = new UserRegistrationDto("Test", "Testesen", "password", "password", "test@mail.no", LocalDate.now());

            Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(user);
            assertTrue(violations.isEmpty());
      }

}
