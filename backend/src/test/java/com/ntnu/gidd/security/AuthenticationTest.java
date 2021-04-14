package com.ntnu.gidd.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntnu.gidd.controller.request.LoginRequest;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = WebSecurity.class)
@ActiveProfiles("test")
public class AuthenticationTest {
    
    private static final String URI = "/auth/";
    private static final String email = "test@mail.com";
    private static final String password = "password123";
    
    @Autowired
    private WebApplicationContext context;
    
    @MockBean
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setup() {
        User testUser = new User();

        testUser.setEmail(email);
        testUser.setPassword(password);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        
        
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testLogin() throws Exception {
    
        System.out.println(userRepository.findByEmail(email));
        
        LoginRequest loginRequest = new LoginRequest(email, password);
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post(URI + "login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(loginJson).with(csrf()))
        .andDo(print())
        .andExpect(status().isOk());

    }
    
    @Test
    public void heii() throws Exception {
        mockMvc.perform(get("/").with(user("user")));
    }
    
}
