package com.ntnu.gidd.controller;

import com.ntnu.gidd.factories.ActivityFactory;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.repository.ActivityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public class ActivityControllerTest {

    private  String URI = "/activities/";

    @Autowired
    private MockMvc mvc;

    private ActivityFactory activityFactory = new ActivityFactory();
    @Autowired
    private ActivityRepository  activityRepository;

    private Activity activity;

    @BeforeEach
    public void setUp() throws Exception {
        activity = activityFactory.getObject();
        assert activity != null;
        activity = activityRepository.save(activity);
    }

    @AfterEach
    public  void cleanUp(){
        activityRepository.delete(activity);
    }
    @WithMockUser(value = "spring")
    @Test
    public void test_activity_controller_getAll_returns_OK_and_a_list_of_activities() throws Exception {
        this.mvc.perform(get(URI).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.[0].title").value(activity.getTitle()));
    }

    @WithMockUser(value = "spring")
    @Test
    public void test_activity_controller_get_returns_OK_and_the_author () throws Exception {
        this.mvc.perform(get(URI+activity.getId()+"/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(activity.getTitle()))
                .andExpect(jsonPath("$.description").value(activity.getDescription()));

    }

}
