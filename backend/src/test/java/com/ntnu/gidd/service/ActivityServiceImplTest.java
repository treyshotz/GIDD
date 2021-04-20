package com.ntnu.gidd.service;

import com.ntnu.gidd.dto.ActivityDto;
import com.ntnu.gidd.dto.ActivityListDto;
import com.ntnu.gidd.factories.ActivityFactory;
import com.ntnu.gidd.factories.TrainingLevelFactory;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.TrainingLevel;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.TrainingLevelRepository;
import com.ntnu.gidd.utils.StringRandomizer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.doesNotHave;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceImplTest {

    @InjectMocks
    private ActivityServiceImpl activityService;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private TrainingLevelRepository trainingLevelRepository;

    ModelMapper modelMapper = new ModelMapper();

    private Activity activity;

    @BeforeEach
    public void setUp() throws Exception {
        activity = new ActivityFactory().getObject();
        assert activity != null;
        lenient().when(activityRepository.save(activity)).thenReturn(activity);
    }

    @Test
    void testActivityServiceImplUpdateActivityAndReturnsUpdatedActivity() {
        TrainingLevel level = activity.getTrainingLevel();
        when(activityRepository.findById(activity.getId())).thenReturn(Optional.ofNullable(activity));
        when(trainingLevelRepository.findTrainingLevelByLevel(level.getLevel())).thenReturn(Optional.of(level));
        activity.setTitle(StringRandomizer.getRandomString(10));

        ActivityDto updateActivity = activityService.updateActivity(activity.getId(),modelMapper.map(activity,ActivityDto.class));

        assertThat(activity.getId()).isEqualTo(updateActivity.getId());

        assertThat(activity.getTitle()).isEqualTo(updateActivity.getTitle());
        assertThat(activity.getDescription()).isEqualTo(updateActivity.getDescription());
        assertThat(activity.getCapacity()).isEqualTo(updateActivity.getCapacity());
    }

    @Test
    void testActivityServiceImplGetActivityByIdReturnsActivity() {
        when(activityRepository.findById(activity.getId())).thenReturn(Optional.ofNullable(activity));

        ActivityDto activityFound = activityService.getActivityById(activity.getId());

        assertThat(activityFound.getId()).isEqualTo(activity.getId());
    }

    @Test
    void testActivityServiceImplGetActivitiesReturnsActivities() throws Exception{

        Activity secondActivity = new ActivityFactory().getObject();
        assert secondActivity != null;
        PageRequest request = PageRequest.of(0,2);
        Page<Activity> activities = new PageImpl<>(List.of(activity, secondActivity), request, 2);

        lenient().when(activityRepository.findAll(PageRequest.of(0, 2))).thenReturn(activities);

        Page<ActivityListDto> getActivities = activityService.getActivities(PageRequest.of(0, 2));

        for (int i = 0; i < activities.getContent().size(); i++){
            assertThat(activities.getContent().get(i).getTitle()).isEqualTo(getActivities.getContent().get(i).getTitle());
        }
    }
}
