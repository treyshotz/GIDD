package com.ntnu.gidd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.ntnu.gidd.controller.request.LoginRequest;
import com.ntnu.gidd.dto.UserPasswordUpdateDto;
import com.ntnu.gidd.factories.UserFactory;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.security.config.JWTConfig;
import com.ntnu.gidd.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerTest {
	
	private static final String URI = "/auth/";
	private static final String password = "password123";
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private JWTConfig jwtConfig;
	
	private User user;
	
	private String refreshToken;
	
	private String accessToken;
	
	@BeforeEach
	void setUp() throws Exception {
		user = new UserFactory().getObject();
		user.setPassword(encoder.encode(password));
		
		user = userRepository.save(user);
		
		LoginRequest loginRequest = new LoginRequest(user.getEmail(), password);
		String loginJson = objectMapper.writeValueAsString(loginRequest);
		
		MvcResult mvcResult = mvc.perform(post(URI + "login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginJson))
				.andReturn();
		
		accessToken = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.token");
		refreshToken = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.refreshToken");
	}
	
	/**
	 * Test that a new access token is returned when authorizing with a valid refresh token.
	 */
	@Test
	void testRefreshTokenWithValidRefreshTokenReturnsNewToken() throws Exception {
		MvcResult mvcResult = mvc.perform(get(URI + "refresh-token")
				.header(jwtConfig.getHeader(), jwtConfig.getPrefix() + refreshToken))
				.andExpect(jsonPath("$.token").isNotEmpty())
				.andReturn();
		
		String newToken = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.token");
		String actualEmail = jwtUtil.getEmailFromToken(newToken);
		
		assertThat(actualEmail).isEqualTo(user.getEmail());
	}
	
	/**
	 * Test that a new access token is not allowed when authorizing with an access token.
	 */
	@Test
	void testRefreshTokenWithAccessTokenIsNotAllowed() throws Exception {
		mvc.perform(get(URI + "refresh-token")
				.header(jwtConfig.getHeader(), jwtConfig.getPrefix() + accessToken))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}
	
	
	/**
	 * Verifies that you can change password if you have the correct token
	 *
	 * @throws Exception
	 */
	@Test
	public void testChangePasswordWithToken() throws Exception {
		LoginRequest loginRequest = new LoginRequest(user.getEmail(), "newPassword");
		String loginJson = objectMapper.writeValueAsString(loginRequest);
		UserPasswordUpdateDto update = new UserPasswordUpdateDto(password, "newPassword");
		
		
		mvc.perform(post("/auth/change-password/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(update))
				.header(jwtConfig.getHeader(), jwtConfig.getPrefix() + accessToken))
				.andExpect(status().isOk());
		
		mvc.perform(post(URI + "login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginJson))
				.andExpect(status().isOk());
	}
	
	/**
	 * Verifies that you cannot change password without a valid token
	 *
	 * @throws Exception
	 */
	@Test
	public void testChangePasswordWithoutTokenFails() throws Exception {
		UserPasswordUpdateDto update = new UserPasswordUpdateDto(password, "newPassword");
		mvc.perform(post("/auth/change-password/")
				.contentType(MediaType.APPLICATION_JSON)
				.header(jwtConfig.getHeader(), jwtConfig.getPrefix() + accessToken))
				.andExpect(status().isBadRequest());
	}
	
	/**
	 * Tests that you cannot change the password if you submit wrong current password
	 *
	 * @throws Exception
	 */
	@Test
	public void testChangePasswordWithWrongOldPasswordFails() throws Exception {
		LoginRequest loginRequest = new LoginRequest(user.getEmail(), "newPassword");
		String loginJson = objectMapper.writeValueAsString(loginRequest);
		UserPasswordUpdateDto update = new UserPasswordUpdateDto("newPassword", "newPassword");
		
		
		mvc.perform(post("/auth/change-password/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(update))
				.header(jwtConfig.getHeader(), jwtConfig.getPrefix() + accessToken))
				.andExpect(status().isNotAcceptable());
		
		mvc.perform(post(URI + "login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginJson))
				.andExpect(status().isUnauthorized());
	}
}