package com.ntnu.gidd.service.activity;

import com.ntnu.gidd.dto.Activity.ActivityDto;
import com.ntnu.gidd.dto.Activity.ActivityListDto;
import com.ntnu.gidd.dto.geolocation.GeoLocationDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.exception.UserNotFoundException;
import com.ntnu.gidd.model.*;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.TrainingLevelRepository;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.service.ActivityImage.ActivityImageService;
import com.ntnu.gidd.service.activity.expression.ActivityExpression;
import com.ntnu.gidd.util.TrainingLevelEnum;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

import static com.querydsl.core.types.dsl.MathExpressions.cos;
import static com.querydsl.core.types.dsl.MathExpressions.acos;
import static com.querydsl.core.types.dsl.MathExpressions.sin;
import static com.querydsl.core.types.dsl.MathExpressions.radians;


@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TrainingLevelRepository trainingLevelRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivityImageService activityImageService;

    @Transactional
    @Override
    public ActivityDto updateActivity(UUID activityId, ActivityDto activity) {
        Activity updateActivity = this.activityRepository.findById(activityId)
                .orElseThrow(ActivityNotFoundExecption::new);
        updateActivity.setTitle(activity.getTitle());
        updateActivity.setDescription(activity.getDescription());
        updateActivity.setStartDate(activity.getStartDate());
        updateActivity.setEndDate(activity.getEndDate());
        updateActivity.setSignupStart(activity.getSignupStart());
        updateActivity.setSignupEnd(activity.getSignupEnd());
        updateActivity.setClosed(activity.isClosed());
        updateActivity.setCapacity(activity.getCapacity());
        if(activity.getLevel()!=null)updateActivity.setTrainingLevel(getTrainingLevel(activity.getLevel()));

        if (activity.getGeoLocation() != null)
            setGeoLocation(activity, updateActivity);


        updateActivity = this.activityRepository.save(updateActivity);
        if (activity.getImages() !=  null) updateActivity.setImages(activityImageService.updateActivityImage(
                activity.getImages(), updateActivity
        ));
        return modelMapper.map(updateActivity,ActivityDto.class);
    }

    private TrainingLevel getTrainingLevel(TrainingLevelEnum level){
        return trainingLevelRepository.findTrainingLevelByLevel(level).
                orElseThrow(() -> new EntityNotFoundException("Traning level does not exist"));
    }

    @Override
    public ActivityDto getActivityById(UUID id) {
       return modelMapper.map(this.activityRepository.findById(id).
               orElseThrow(ActivityNotFoundExecption::new), ActivityDto.class);
}
    @Override
    public Page<ActivityListDto> getActivities(Predicate predicate, Pageable pageable) {
        return this.activityRepository.findAll(predicate, pageable)
                .map(s -> modelMapper.map(s, ActivityListDto.class));
    }

    @Override
    public Page<ActivityListDto> getActivities(Predicate predicate, Pageable pageable, GeoLocation position, Double range) {
        
        predicate = ActivityExpression.of(predicate)
                .closestTo(position)
                .range(range)
                .toPredicate();

        return getActivities(predicate, pageable);
    }


    @Override
    public ActivityDto saveActivity(ActivityDto activity, String creatorEmail) {
        Activity newActivity = modelMapper.map(activity, Activity.class);
        User user = userRepository.findByEmail(creatorEmail).orElseThrow(UserNotFoundException::new);
        newActivity.setId(UUID.randomUUID());
        newActivity.setCreator(user);
        newActivity.setHosts(List.of());
        if(activity.getLevel()!= null)newActivity.setTrainingLevel(getTrainingLevel(activity.getLevel()));

        if (activity.getGeoLocation() != null)
            setGeoLocation(activity, newActivity);


        newActivity  = this.activityRepository.save(newActivity);
        if (activity.getImages() !=  null) newActivity.setImages(activityImageService.saveActivityImage(
                newActivity.getImages(), newActivity
        ));
        return modelMapper.map(newActivity, ActivityDto.class);
    }

    private void setGeoLocation(ActivityDto activity, Activity newActivity) {
        GeoLocationDto geoLocationDto = activity.getGeoLocation();
        GeoLocation geoLocation = new GeoLocation(geoLocationDto
                                                          .getLat(), activity.getGeoLocation()
                .getLng());

        newActivity.setGeoLocation(geoLocation);
    }

    @Override
    public void deleteActivity(UUID id){
        this.activityRepository.deleteById(id);
    }
}


