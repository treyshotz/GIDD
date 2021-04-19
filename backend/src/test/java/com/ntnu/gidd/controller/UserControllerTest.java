package com.ntnu.gidd.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntnu.gidd.dto.UserRegistrationDto;
import com.ntnu.gidd.factories.UserFactory;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.security.UserDetailsImpl;
import com.ntnu.gidd.utils.StringRandomizer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

      private String URI = "/users/";

      @Autowired
      private MockMvc mockMvc;

      @Autowired
      private ObjectMapper objectMapper;

      @Autowired
      private UserRepository userRepository;

      private User user;

      private UserFactory userFactory = new UserFactory();

      private String firstName;

      private String surname;

      private LocalDate birthDate;

      /**
       * Setting up variables that is the same for all tests
       */
      @BeforeEach
      public void setUp() throws Exception {
            user = userFactory.getObject();
            assert user != null;
            userRepository.save(user);
            firstName = "Test";
            surname = "Testersen";
            birthDate = LocalDate.now();
      }

      /**
       * Cleans up the saved users after each test
       */
      @AfterEach
      public void cleanUp(){
            userRepository.deleteAll();
      }

      /**
       * Provides a stream of Valid emails to provide parameterized test
       * @return Stream of valid emails
       */
      private static Stream<Arguments> provideValidEmails() {
            return Stream.of(
                  Arguments.of("test123@mail.com"),
                  Arguments.of("test1.testesen@mail.com"),
                  Arguments.of("test_1234-testesen@mail.com")
            );
      }

      /**
       * Provides a stream of Invalid emails to provide parameterized test
       * @return Stream of invalid emails
       */
      private static Stream<Arguments> provideInvalidEmails() {
            return Stream.of(
                  Arguments.of("test123.no"),
                  Arguments.of("test@"),
                  Arguments.of("test@mail..com")
            );
      }
      
      /**
       * Test that you can create a user with valid input
       * @throws Exception from post request
       */
      @ParameterizedTest
      @MethodSource("provideValidEmails")
      @WithMockUser(value = "spring")
      public void testCreateUserWithValidEmail(String email) throws Exception {
            String password = "Ithinkthisisvalid123";
            String matchingPassword = "Ithinkthisisvalid123";

            UserRegistrationDto validUser = new UserRegistrationDto(firstName, surname, password, matchingPassword, email, birthDate);

            mockMvc.perform(post(URI)
                    .with(csrf())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(validUser)))
                  .andExpect(status().isCreated());
      }

      
      /**
       * Test that a user can be created, but the same email cannot be used two times
       * @throws Exception from post request
       */
      @Test
      @WithMockUser(value = "spring")
      public void testCreateUserTwoTimesFails() throws Exception {
            User user = userFactory.getObject();
            assert user != null;
            userRepository.save(user);

            String email = user.getEmail();
            String password = "Ithinkthisisvalid123";

            UserRegistrationDto validUser = new UserRegistrationDto(firstName, surname, password, password, email, birthDate);


            mockMvc.perform(post(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validUser)))
                    .andExpect(status().is4xxClientError());
      }

      /**
       * Test that a user cannot be created if email is on a wrong format
       * @throws Exception
       */
      @ParameterizedTest
      @MethodSource("provideInvalidEmails")
      @WithMockUser(value = "spring")
      public void testCreateUserWithInvalidEmail(String email) throws Exception {
            String password = "Ithinkthisisvalid123";

            UserRegistrationDto invalidUser = new UserRegistrationDto(firstName, surname, password, password, email, birthDate);

            mockMvc.perform(post(URI)
                    .with(csrf())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(invalidUser)))
                  .andExpect(status().is4xxClientError());
      }

      /**
       * Tests that a user cannot be created when the password fields isn't matching
       * @throws Exception
       */
      @Test
      public void testCreateUserWithNotMatchingPasswordFails() throws Exception {
            String email = "test123@test.no";
            String password = "Ithinkthisisvalid123";

            UserRegistrationDto invalidUser = new UserRegistrationDto(firstName, surname, password, "not the same", email, birthDate);

            mockMvc.perform(post(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidUser)))
                    .andExpect(status().is4xxClientError());
      }

      /**
       * Tests that get return a correct user according to token
       * @throws Exception
       */
      @Test
      public void testGetUserReturnsCorrectUser() throws Exception {
            UserDetails userDetails = UserDetailsImpl.builder().email(user.getEmail()).build();
            mockMvc.perform(get(URI+"me/")
                    .with(user(userDetails)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(user.getId().toString()));
      }

      /**
       * Tests that put updated a user and returns the updated user info
       * @throws Exception
       */
      @Test
      @WithMockUser(value = "spring")
      public void testUpdateUserUpdatesUserAndReturnUpdatedData() throws Exception {
            String surname = StringRandomizer.getRandomString(8);
            user.setSurname(surname);
            mockMvc.perform(put(URI+user.getId()+"/")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(user)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(user.getId().toString()));
      }

}
