package com.ntnu.gidd.controller.Host;

import com.ntnu.gidd.factories.ActivityFactory;
import com.ntnu.gidd.factories.UserFactory;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserHostControllerTest {

    private String getURI(User user) {
        return "/user/" + user.getId().toString() + "/hosts/";
    }

    @Autowired
    private MockMvc mvc;

    private ActivityFactory activityFactory = new ActivityFactory();
    private UserFactory userFactory = new UserFactory();

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() throws Exception {
        user = userFactory.getObject();
        assert user != null;
        user.setActivities(List.of(Objects.requireNonNull(activityFactory.getObject())));
        activityRepository.saveAll(user.getActivities());
        user = userRepository.save(user);
    }

    @AfterEach
    public void cleanUp(){
        activityRepository.deleteAll();
        userRepository.deleteAll();
    }

    @WithMockUser(value = "spring")
    @Test
    public void testUserHostControllerGetAllReturnsListOActivities() throws Exception {
        this.mvc.perform(get(getURI(user)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.[0].title").value(user.getActivities().get(0).getTitle()));
    }


    @WithMockUser(value = "spring")
    @Test
    public void testUserHostGetReturnsTheWantedActivity() throws Exception {
        this.mvc.perform(get(getURI(user)+user.getActivities().get(0).getId().toString()+"/")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(user.getActivities().get(0).getTitle()));

    }

    @WithMockUser(value = "spring")
    @Test
    public void testUserHostControllerDeletesHostAndReturnsUpdatedList() throws Exception {
        ArrayList<Activity> list = new ArrayList<>(user.getActivities());
        Activity deleteActivity = activityFactory.getObject();
        assert deleteActivity != null;
        list.add(deleteActivity);
        user.setActivities(list);
        activityRepository.save(deleteActivity);
        userRepository.save(user);
        this.mvc.perform(delete(getURI(user)+user.getActivities().get(0).getId().toString()+"/")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(deleteActivity.getTitle()));

    }
}
