package com.ntnu.gidd.controller.followers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntnu.gidd.factories.UserFactory;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FollowingControllerTest {

    private static final String URI = "/users/me/following/";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    private UserFactory userFactory = new UserFactory();

    private ObjectMapper objectMapper = new ObjectMapper();

    private User actor;

    private User subject;

    private UserDetailsImpl actorUserDetails;

    private String subjectIdRequestBody;

    @BeforeEach
    void setUp() throws Exception {
        actor = userFactory.getObject();
        subject = userFactory.getObject();

        actor = userRepository.saveAndFlush(actor);
        subject = userRepository.saveAndFlush(subject);

        actorUserDetails = UserDetailsImpl.builder().id(actor.getId())
                .email(actor.getEmail())
                .build();
        subjectIdRequestBody = objectMapper.writeValueAsString(subject.getId());
    }
    
    @Test
    public void testFollowUserWhenAttemptingToFollowSelfReturnsStatus400() throws Exception {
        String actorIdRequestBody = objectMapper.writeValueAsString(actor.getId());

        mvc.perform(post(URI)
                            .with(user(actorUserDetails))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(actorIdRequestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFollowUserWhenValidRequestReturnsStatus201() throws Exception {
        mvc.perform(post(URI)
                            .with(user(actorUserDetails))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(subjectIdRequestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    public void testFollowUserWhenValidRequestAddsSubjectToActorsFollowing() throws Exception {
        mvc.perform(post(URI)
                            .with(user(actorUserDetails))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(subjectIdRequestBody));

        actor = userRepository.findById(actor.getId()).get();

        assertThat(actor.getFollowing()).contains(subject);
    }

    @Test
    public void testFollowUserWhenValidRequestAddsActorToSubjectsFollowers() throws Exception {
        mvc.perform(post(URI)
                            .with(user(actorUserDetails))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(subjectIdRequestBody));

        subject = userRepository.findById(subject.getId()).get();

        assertThat(subject.getFollowers()).contains(actor);
    }


    @Test
    public void testFollowUserWhenUnauthorizedReturnsHttp401() throws Exception {
        mvc.perform(post(URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(subjectIdRequestBody))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void testFollowUserWhenSubjectNotFoundReturnsStatus404() throws Exception {
        String nonExistentUserIdRequestBody = objectMapper.writeValueAsString(UUID.randomUUID());

        mvc.perform(post(URI)
                            .with(user(actorUserDetails))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(nonExistentUserIdRequestBody))
            .andExpect(status().isNotFound());
    }
}