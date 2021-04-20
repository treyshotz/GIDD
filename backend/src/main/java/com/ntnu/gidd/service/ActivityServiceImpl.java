package com.ntnu.gidd.service;

import com.ntnu.gidd.dto.ActivityDto;
import com.ntnu.gidd.dto.ActivityListDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.TrainingLevel;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.TrainingLevelRepository;
import com.ntnu.gidd.util.TrainingLevelEnum;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TrainingLevelRepository trainingLevelRepository;

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

        return modelMapper.map(this.activityRepository.save(updateActivity),ActivityDto.class);
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
    public Page<ActivityListDto> getActivities(Pageable pageable) {
        return this.activityRepository.findAll(pageable).map(s -> modelMapper.map(s, ActivityListDto.class));
    }

    @Override
    public ActivityDto saveActivity(ActivityDto activity) {
        Activity newActivity = modelMapper.map(activity, Activity.class);
        newActivity.setId(UUID.randomUUID());
        newActivity.setHosts(List.of());
        if(activity.getLevel()!= null)newActivity.setTrainingLevel(getTrainingLevel(activity.getLevel()));
        return modelMapper.map(this.activityRepository.save(newActivity), ActivityDto.class);
    }

    @Override
    public void deleteActivity(UUID id){
        this.activityRepository.deleteById(id);
    }
}


