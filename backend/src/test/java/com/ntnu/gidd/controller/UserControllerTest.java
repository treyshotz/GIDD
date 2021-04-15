package com.ntnu.gidd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntnu.gidd.factories.UserFactory;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public class UserControllerTest {

    private  String URI = "/users/";

    @Autowired
    private MockMvc mvc;

    private UserFactory userFactory = new UserFactory();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    public void setUp() throws Exception {
        user = userFactory.getObject();
        assert user != null;
        user = userRepository.save(user);
    }
    @WithMockUser(value = "spring")
    @Test
    public void testUserControllerUpdateUserReturns200okAndTheUpdatedUser() throws Exception {

        User testUser = userFactory.getObject();

        this.mvc.perform(put(URI + user.getId() +"/")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.firstName").value(testUser.getFirstName()))
                .andExpect(jsonPath("$.surname").value(testUser.getSurname()));

    }
}
