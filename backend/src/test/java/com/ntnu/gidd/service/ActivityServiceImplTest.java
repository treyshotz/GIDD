package com.ntnu.gidd.service;

import com.ntnu.gidd.dto.ActivityListDto;
import com.ntnu.gidd.factories.ActivityFactory;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.repository.ActivityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceImplTest {

    @InjectMocks
    private ActivityServiceImpl activityService;

    @Mock
    private ActivityRepository activityRepository;

    private Activity activity;

    @BeforeEach
    public void setUp() throws Exception {
        activity = new ActivityFactory().getObject();
        assert activity != null;

        when(activityRepository.save(activity)).thenReturn(activity);
        activityRepository.save(activity);
    }

    @AfterEach public void cleanUp(){
        activityRepository.delete(activity);
    }

    @Test
    void test_activity_service_impl_update_activity_and_returns_updatedActivity() throws Exception{
        when(activityRepository.findById(activity.getId())).thenReturn(Optional.ofNullable(activity));

        Activity updateActivity = new ActivityFactory().getObject();
        assert updateActivity != null;

        activityService.updateActivity(activity.getId(), updateActivity);

        assertThat(activity.getId()).isNotEqualByComparingTo(updateActivity.getId());

        assertThat(activity.getTitle()).isEqualTo(updateActivity.getTitle());
        assertThat(activity.getDescription()).isEqualTo(updateActivity.getDescription());
        assertThat(activity.getCapacity()).isEqualTo(updateActivity.getCapacity());
    }

    @Test
    void test_activity_service_impl_get_activity_by_id_returns_activity() throws Exception{
        when(activityRepository.findById(activity.getId())).thenReturn(Optional.ofNullable(activity));

        Activity activityFound = activityService.getActivityById(activity.getId());

        assertThat(activityFound).isEqualTo(activity);
    }

    @Test
    void test_activity_service_impl_get_activities_returns_activities() throws Exception{

        Activity secondActivity = new ActivityFactory().getObject();
        assert secondActivity != null;

        List<Activity> activities = List.of(activity, secondActivity);

        when(activityRepository.findAll()).thenReturn(activities);

        List<ActivityListDto> getActivities = activityService.getActivties();

        for (int i = 0; i < activities.size(); i++){
            assertThat(activities.get(i).getTitle()).isEqualTo(getActivities.get(i).getTitle());
        }

    }

}
