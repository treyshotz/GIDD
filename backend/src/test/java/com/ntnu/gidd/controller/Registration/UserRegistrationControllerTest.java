package com.ntnu.gidd.controller.Registration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.ntnu.gidd.factories.ActivityFactory;
import com.ntnu.gidd.factories.UserFactory;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.Registration;
import com.ntnu.gidd.model.RegistrationId;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.RegistrationRepository;
import com.ntnu.gidd.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public class UserRegistrationControllerTest {

  private String URI = "/users/";

  @Autowired
  private MockMvc mvc;

  @Autowired
  RegistrationRepository registrationRepository;

  @Autowired
  ActivityRepository activityRepository;

  @Autowired
  UserRepository userRepository;

  private ActivityFactory activityFactory = new ActivityFactory();
  private UserFactory userFactory = new UserFactory();
  private RegistrationId registrationId = new RegistrationId();
  private Registration registration = new Registration();
  private Activity activity;
  private User user;


  @BeforeEach
  public void setup() throws Exception {
    activity = activityFactory.getObject();
    assert activity != null;
    activity = activityRepository.save(activity);

    user = userFactory.getObject();
    assert user != null;
    user = userRepository.save(user);

    registration.setUser(user);
    registration.setActivity(activity);

    registrationId.setActivityId(activity.getId());
    registrationId.setUserId(user.getId());
    registration.setRegistrationId(registrationId);
    registration = registrationRepository.save(registration);
  }

  @AfterEach
  public void cleanup() {
    registrationRepository.delete(registration);
    activityRepository.delete(activity);
    userRepository.delete(user);
  }


  @WithMockUser(value = "spring")
  @Test
  public void testUserRegistrationControllerGetRegistrationsForUser() throws Exception {
    this.mvc.perform(get(URI + user.getId() + "/registrations/")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$..user.id").value(user.getId().toString()))
        .andExpect(jsonPath("$..activity.id").value(activity.getId().toString()));
  }

  @WithMockUser(value = "spring")
  @Test
  public void testUserRegistrationControllerGetRegistrationWithCompositeIdUser() throws Exception {
    this.mvc.perform(get(URI + user.getId() + "/registrations/" + activity.getId() + "/")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.user.id").value(user.getId().toString()))
        .andExpect(jsonPath("$.activity.id").value(activity.getId().toString()));
  }
}
