package com.ntnu.gidd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
public class ActivityControllerTest {

    private  String URI = "/activities/";

    @Autowired
    private MockMvc mvc;

    private ActivityFactory activityFactory = new ActivityFactory();

    @Autowired
    private ActivityRepository  activityRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void testActivityControllerGetAllReturnsOKAndAListOfActivities() throws Exception {
        this.mvc.perform(get(URI).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$.[0].title").value(activity.getTitle()));
    }

    @WithMockUser(value = "spring")
    @Test
    public void testActivityControllerGetReturnsOKAndTheAuthor () throws Exception {
        this.mvc.perform(get(URI+activity.getId()+"/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(activity.getTitle()))
                .andExpect(jsonPath("$.description").value(activity.getDescription()));

    }


    @WithMockUser(value = "spring")
    @Test
    public void testActivityControllerSaveReturn201ok() throws Exception {

        Activity testActivity = activityFactory.getObject();

        this.mvc.perform(post(URI)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testActivity)))
            .andExpect(status().isCreated());

    }


    @WithMockUser(value = "spring")
    @Test
    public void testActivityControllerDeleteActivityAndReturnsOk() throws Exception {

        Activity testActivity = activityFactory.getObject();
        assert testActivity != null;
        testActivity = activityRepository.save(testActivity);

        this.mvc.perform(delete(URI + testActivity.getId() + "/")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    }

}
