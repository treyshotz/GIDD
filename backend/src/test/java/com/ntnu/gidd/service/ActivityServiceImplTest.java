package com.ntnu.gidd.service;

import com.ntnu.gidd.dto.Activity.ActivityDto;
import com.ntnu.gidd.dto.Activity.ActivityListDto;
import com.ntnu.gidd.factories.ActivityFactory;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.GeoLocation;
import com.ntnu.gidd.model.GeoLocationId;
import com.ntnu.gidd.model.TrainingLevel;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.TrainingLevelRepository;
import com.ntnu.gidd.service.activity.ActivityServiceImpl;
import com.ntnu.gidd.utils.JpaUtils;
import com.ntnu.gidd.utils.StringRandomizer;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    private Predicate predicate;
    private Pageable pageable;

    @BeforeEach
    public void setUp() throws Exception {
        activity = new ActivityFactory().getObject();
        assert activity != null;
        lenient().when(activityRepository.save(activity)).thenReturn(activity);
        predicate = JpaUtils.getEmptyPredicate();
        pageable = JpaUtils.getDefaultPageable();
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
        List<Activity> testList = List.of(activity, secondActivity);
        Page<Activity> activities = new PageImpl<>(testList, pageable, testList.size());

        lenient().when(activityRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(activities);

        Page<ActivityListDto> getActivities = activityService.getActivities(predicate, pageable);

        for (int i = 0; i < activities.getContent().size(); i++){
            assertThat(activities.getContent().get(i).getTitle()).isEqualTo(getActivities.getContent().get(i).getTitle());
        }
    }

    @Test
    void testActivityServiceImplGetActivitiesReturnsActivitiesWithGeoLocation() throws Exception{

        Activity secondActivity = new ActivityFactory().getObject();
        assert secondActivity != null;
        List<Activity> testList = List.of(activity, secondActivity);
        Page<Activity> activities = new PageImpl<>(testList, pageable, testList.size());

        lenient().when(activityRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(activities);

        GeoLocation position = new GeoLocation(0.0, 0.0);
        Page<ActivityListDto> getActivities = activityService.getActivities(predicate, pageable, position,
                                                                            1.0);

        for (int i = 0; i < activities.getContent().size(); i++){
            assertThat(activities.getContent().get(i).getTitle()).isEqualTo(getActivities.getContent().get(i).getTitle());
        }
    }

}
