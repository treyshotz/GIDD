package com.ntnu.gidd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntnu.gidd.factories.ActivityFactory;
import com.ntnu.gidd.factories.UserFactory;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.TrainingLevel;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.service.traininglevel.TrainingLevelService;
import com.ntnu.gidd.security.UserDetailsImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Objects;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


@SpringBootTest(webEnvironment = MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ActivityControllerTest {

    private  String URI = "/activities/";
    private final String TITLE = "Test activity";

    @Autowired
    private MockMvc mvc;

    private ActivityFactory activityFactory = new ActivityFactory();
    private UserFactory userFactory = new UserFactory();
    @Autowired
    private ActivityRepository  activityRepository;



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingLevelService trainingLevelService;

    @Autowired
    private ObjectMapper objectMapper;

    private Activity activity;

    @BeforeEach
    public void setUp() throws Exception {
        activity = activityFactory.getObject();
        assert activity != null;
        activity.setTitle(TITLE);
        activity = activityRepository.save(activity);
    }

    @AfterEach
    public void cleanUp(){
        activityRepository.deleteAll();
    }

    @WithMockUser(value = "spring")
    @Test
    public void testActivityControllerGetAllReturnsOKAndAListOfActivities() throws Exception {
        this.mvc.perform(get(URI).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.[0].title").value(activity.getTitle()));
    }

    @WithMockUser(value = "spring")
    @Test
    public void testActivityControllerGetReturnsOKAndTheAuthor () throws Exception {
        this.mvc.perform(get(URI+activity.getId().toString()+"/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(activity.getTitle()))
                .andExpect(jsonPath("$.description").value(activity.getDescription()));



    }


    @WithMockUser(value = "spring")
    @Test
    public void testActivityControllerSaveReturn201ok() throws Exception {

        Activity testActivity = activityFactory.getObject();
        User user = userFactory.getObject();
        assert user !=null;
        userRepository.save(user);
        UserDetails userDetails = UserDetailsImpl.builder().email(user.getEmail()).build();
        assert testActivity != null;
        this.mvc.perform(post(URI).with(user(userDetails))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(testActivity)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value(testActivity.getTitle()))
            .andExpect(jsonPath("$.creator.firstName").value(user.getFirstName()))
            .andExpect(jsonPath("$.hosts").isArray());

    }


    @WithMockUser(value = "spring")
    @Test
    public void testActivityControllerDeleteActivityAndReturnsOk() throws Exception {

        Activity testActivity = activityFactory.getObject();
        assert testActivity != null;
        testActivity = activityRepository.save(testActivity);

        this.mvc.perform(delete(URI + testActivity.getId().toString() + "/")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Activity has been deleted"));

    }

    @WithMockUser(value = "spring")
    @Test
    public void testActivityControllerFiltersOnWantedFieldsForTitle() throws Exception {
        Activity dummy = activityFactory.getObject();
        dummy.setTitle("Dummy title");
        activityRepository.save(dummy);

        this.mvc.perform(get(URI).accept(MediaType.APPLICATION_JSON)
                .param("title", activity.getTitle()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content.[0].title").value(activity.getTitle()));
    }

    @Test
    @WithMockUser
    public void testFilterActivitiesPartialTitleReturnsCorrectResults() throws Exception {
        Activity dummy = activityFactory.getObject();
        dummy.setTitle("Dummy title");
        activityRepository.save(dummy);
        mvc.perform(get(URI)
                            .accept(MediaType.APPLICATION_JSON)
                .param("title", TITLE.substring(0, TITLE.length() - 1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content.[0].title").value(activity.getTitle()));
    }

    @Test
    @WithMockUser
    public void testFilterActivitiesByStartDateAfterReturnActivitiesStartingAfterGivenDateTime() throws Exception {
        Activity dummy = activityFactory.getObject();
        dummy.setStartDate(ZonedDateTime.now().minusDays(100));
        activityRepository.save(dummy);


        mvc.perform(get(URI).accept(MediaType.APPLICATION_JSON)
                .param("startDateAfter", String.valueOf(activity.getStartDate().minusHours(1))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content.[0].title").value(activity.getTitle()));
    }

    @Test
    @WithMockUser
    public void testFilterActivitiesByStartDateBeforeReturnActivitiesStartingBeforeGivenDateTime() throws Exception {
        Activity expectedActivity = activityFactory.getObject();
        expectedActivity.setStartDate(ZonedDateTime.now().minusDays(100));
        activityRepository.save(expectedActivity);

        mvc.perform(get(URI).accept(MediaType.APPLICATION_JSON)
                .param("startDateBefore", String.valueOf(activity.getStartDate().minusHours(1))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content.[0].title").value(expectedActivity.getTitle()));
    }


    @Test
    @WithMockUser
    public void testFilterActivitiesByStartDateBetweenReturnActivitiesStartingInRange() throws Exception {
        Activity dummy = activityFactory.getObject();
        dummy.setStartDate(ZonedDateTime.now().minusDays(100));
        activityRepository.save(dummy);

        Activity between = activityFactory.getObject();
        between.setStartDate(ZonedDateTime.now().minusDays(50));
        activityRepository.save(between);

        // Due to decimal precision these where sometimes rounded down/up
        // causing the test to fail spontaneously

        mvc.perform(get(URI).accept(MediaType.APPLICATION_JSON)
                .param("startDateAfter", String.valueOf(dummy.getStartDate().plusHours(1)))
                .param("startDateBefore", String.valueOf(activity.getStartDate().minusHours(1))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content.[0].title").value(between.getTitle()));
    }

    // TODO: test with multiple levels
    @Test
    @WithMockUser
    public void testFilterByActivityTrainingLevelReturnsActivitiesWithGivenTrainingLevel() throws Exception {

        TrainingLevel mediumTrainingLevel = trainingLevelService.getTrainingLevelMedium();
        activity.setTrainingLevel(mediumTrainingLevel);
        activityRepository.save(activity);

        TrainingLevel lowTrainingLevel = trainingLevelService.getTrainingLevelLow();
        Activity dummy = activityFactory.getObject();
        dummy.setTrainingLevel(lowTrainingLevel);
        activityRepository.save(dummy);


        mvc.perform(get(URI).accept(MediaType.APPLICATION_JSON)
                .param("trainingLevel.level", String.valueOf(mediumTrainingLevel.getLevel())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content.[0].title").value(activity.getTitle()));
    }
}
