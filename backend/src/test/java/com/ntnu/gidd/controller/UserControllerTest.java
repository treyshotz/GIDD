package com.ntnu.gidd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntnu.gidd.dto.UserRegistrationDto;
import com.ntnu.gidd.factories.UserRegistrationFactory;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;



import java.time.LocalDate;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public class UserControllerTest {

      private String URI = "/users";

      @Autowired
      private MockMvc mockMvc;

      @Autowired
      private ObjectMapper objectMapper;

      @Autowired
      private UserRepository userRepository;

      private ModelMapper modelMapper = new ModelMapper();

      private UserRegistrationFactory userFactory = new UserRegistrationFactory();

      private UserRegistrationDto user;

      /**
       * Sets up a test user before each test
       * @throws Exception
       */
      @BeforeEach
      public void setUp() throws Exception {

      }

      @Test
      public void testCreateUserWithValidInput() throws Exception {
            String email = "test123@test.no";
            String firstName = "tester";
            String surname = "Testersen";
            String password = "Ithinkthisisvalid123";
            LocalDate birthDate = LocalDate.now();

            UserRegistrationDto validUser = new UserRegistrationDto(firstName, surname, password, password, email, birthDate);

            mockMvc.perform(post(URI)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(validUser)))
                  .andExpect(status().isOk());

      }
}
