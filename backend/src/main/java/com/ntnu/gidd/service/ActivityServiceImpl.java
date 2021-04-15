package com.ntnu.gidd.service;

import com.ntnu.gidd.dto.ActivityDto;
import com.ntnu.gidd.dto.ActivityListDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.TrainingLevel;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.TrainingLevelRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TrainingLevelRepository trainingLevelRepository;

    @Override
    public Activity updateActivity(UUID activityId, Activity activity) {
        Activity updateActivity = this.activityRepository.findById(activityId).orElseThrow(
                () -> new ActivityNotFoundExecption("This activity does not exist"));
        updateActivity.setTitle(activity.getTitle());
        updateActivity.setDescription(activity.getDescription());
        updateActivity.setStart_date(activity.getStart_date());
        updateActivity.setEnd_date(activity.getEnd_date());
        updateActivity.setSignup_start(activity.getSignup_start());
        updateActivity.setSignup_end(activity.getSignup_end());
        updateActivity.setClosed(activity.isClosed());
        updateActivity.setCapacity(activity.getCapacity());
        updateActivity.setTrainingLevel(getTrainingLevel(activity));

        return this.activityRepository.save(updateActivity);
    }

    private TrainingLevel getTrainingLevel(Activity activity){
        return trainingLevelRepository.findTraningLevelByLevel(activity.getTrainingLevel().getLevel()).
                orElseThrow(() -> new EntityNotFoundException("Traning level does not exist"));
    }
    Activity addActivity(Activity activity){
       return this.activityRepository.save(activity);
    }


    @Override
    public ActivityDto getActivityById(UUID id) {
       return modelMapper.map(this.activityRepository.findById(id).
               orElseThrow(()-> new ActivityNotFoundExecption("This activity does not exist")),
               ActivityDto.class);
}
    @Override
    public List<ActivityListDto> getActivties() {
        return this.activityRepository.findAll().stream().map(activity->modelMapper.map(activity, ActivityListDto.class))
                .collect(Collectors.toList());
    }
}


