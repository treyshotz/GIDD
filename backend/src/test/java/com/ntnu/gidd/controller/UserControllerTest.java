package com.ntnu.gidd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntnu.gidd.dto.UserRegistrationDto;
import com.ntnu.gidd.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

      private String URI = "/users";

      @Autowired
      private MockMvc mockMvc;

      @Autowired
      private ObjectMapper objectMapper;

      @Autowired
      private UserRepository userRepository;
      
      @BeforeEach
      public void setUp(){
      }

      /**
       * Cleans up the saved users after each test
       */
      @AfterEach
      public void cleanUp(){
            userRepository.deleteAll();
      }

      
      /**
       * Test that you can create a user with valid input
       * @throws Exception from post request
       */
      @WithMockUser(value = "spring")
      @Test
      public void testCreateUserWithValidEmail() throws Exception {
            List<String> emails = Arrays.asList("test@mail.com", "test.testesen@mail.com", "test_123-testesen@mail.com");
            String firstName = "tester";
            String surname = "Testersen";
            String password = "Ithinkthisisvalid123";
            LocalDate birthDate = LocalDate.now();

            for (String email : emails) {
                  UserRegistrationDto validUser = new UserRegistrationDto(firstName, surname, password, password, email, birthDate);

                  mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                        .andExpect(status().isOk());
            }
      }
      
      /**
       * Test that a user can be created, but the same email cannot be used two times
       * @throws Exception from post request
       */
      @WithMockUser(value = "spring")
      @Test
      public void testCreateUserTwoTimesFails() throws Exception {
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


            mockMvc.perform(post(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validUser)))
                    .andExpect(status().is4xxClientError());
      }

      /**
       * Test that a user cannot be created if email is on a wrong format
       * @throws Exception
       */
      @Test
      public void testCreateUserWithInvalidEmail() throws Exception {
            List<String> emails = Arrays.asList("test123.no", "test@", "test@mail..com");
            String firstName = "tester";
            String surname = "Testersen";
            String password = "Ithinkthisisvalid123";
            LocalDate birthDate = LocalDate.now();

            for (String email : emails) {
                  UserRegistrationDto invalidUser = new UserRegistrationDto(firstName, surname, password, password, email, birthDate);

                  mockMvc.perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                        .andExpect(status().is4xxClientError());
            }
      }

      /**
       * Tests that a user cannot be created when the password fields isn't matching
       * @throws Exception
       */
      @Test
      public void testCreateUserWithNotMatchingPasswordFails() throws Exception {
            String email = "test123@test.no";
            String firstName = "tester";
            String surname = "Testersen";
            String password = "Ithinkthisisvalid123";
            LocalDate birthDate = LocalDate.now();
      
            UserRegistrationDto invalidUser = new UserRegistrationDto(firstName, surname, password, "not the same", email, birthDate);
      
            mockMvc.perform(post(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidUser)))
                    .andExpect(status().is4xxClientError());
      }
}
