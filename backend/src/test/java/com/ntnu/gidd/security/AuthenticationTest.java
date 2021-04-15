package com.ntnu.gidd.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntnu.gidd.controller.ActivityController;
import com.ntnu.gidd.controller.request.LoginRequest;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = WebSecurity.class)
@ActiveProfiles("test")
public class AuthenticationTest {
	
	private static final String URI = "/auth/";
	private static final String email = "test@mail.com";
	private static final String password = "password123";
	
	@MockBean
	private UserRepository userRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private JWTConfig jwtConfig;
	
	private JwtUtil jwtUtil;
	
	private User testUser;
	
	@BeforeEach
	public void setup() {
		jwtUtil = new JwtUtil(jwtConfig);
		testUser = User.builder()
				.email(email)
				.password(encoder.encode(password))
				.build();
		
		when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
	}
	
	/**
	 * Test that you can login with a user that does exist
	 *
	 * @throws Exception
	 */
	@Test
	public void testLoginWithCorrectCredentialsReturnsJwtToken() throws Exception {
		LoginRequest loginRequest = new LoginRequest(email, password);
		String loginJson = objectMapper.writeValueAsString(loginRequest);
		
		MvcResult mvcResult = mockMvc.perform(post(URI + "login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginJson))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();
		
		String token = mvcResult.getResponse().getHeader(jwtConfig.getHeader());
		
		assertThat(token).isNotNull();
	}
	
	/**
	 * Test that the jwt returned is created for the user logging in.
	 */
	@Test
	public void testLoginReturnsJwtTokenWithCorrectContent() throws Exception {
		LoginRequest loginRequest = new LoginRequest(email, password);
		String loginJson = objectMapper.writeValueAsString(loginRequest);
		
		MvcResult mvcResult = mockMvc.perform(post(URI + "login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginJson))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();
		
		String token = mvcResult.getResponse().getHeader(jwtConfig.getHeader());
		String actualEmail = jwtUtil.decodeToken(token);
		
		assertThat(actualEmail).isEqualTo(testUser.getEmail());
	}
	
	/**
	 * Tests that you cannot login with a user that does not exist
	 */
	@Test
	public void testLoginWithoutARegisteredUserReturnsUnauthorized() throws Exception {
		LoginRequest loginRequest = new LoginRequest("feil", "bruker");
		String loginJson = objectMapper.writeValueAsString(loginRequest);
		
		mockMvc.perform(post(URI + "login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginJson))
				.andExpect(status().isUnauthorized());
	}
	
	/**
	 * Test that attempting to login with incorrect password returns 401 Unauthorized.
	 */
	@Test
	public void testLoginWhenPasswordIsIncorrectReturnsUnauthorized() throws Exception {
		String incorrectPassword = "wrongpassword";
		testUser.setPassword(encoder.encode(incorrectPassword));
		
		LoginRequest loginRequest = new LoginRequest(testUser.getEmail(), incorrectPassword);
		String loginJson = objectMapper.writeValueAsString(loginRequest);
		
		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
		
		mockMvc.perform(post(URI + "login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginJson))
				.andExpect(status().isUnauthorized());
	}
	
	/**
	 * Test that attempting to login with incorrect email returns 401 Unauthorized.
	 */
	@Test
	public void testLoginWithWrongEmailReturnsUnauthorized() throws Exception {
		LoginRequest loginRequest = new LoginRequest("feil", password);
		String loginJson = objectMapper.writeValueAsString(loginRequest);
		
		mockMvc.perform(post((URI + "login"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginJson))
				.andExpect(status().isUnauthorized());
		
	}
	
	@Test
	@Disabled
	public void testLoginWithCorrectCredentialsAndCanAccessContent() throws Exception {
		LoginRequest loginRequest = new LoginRequest(email, password);
		String loginJson = objectMapper.writeValueAsString(loginRequest);
		
		MvcResult response = mockMvc.perform(post((URI + "login"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginJson))
				.andExpect(status().isOk())
				.andReturn();
		
		String token = response.getResponse().getHeader(jwtConfig.getHeader());
		assert token != null;
		
		mockMvc.perform(get("/api/activities/")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", token))
				.andExpect(status().isOk());
	}
}
