package com.ntnu.gidd.service;

import com.ntnu.gidd.dto.ActivityListDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    ModelMapper modelMapper = new ModelMapper();

    private ActivityRepository activityRepository;

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

        return this.activityRepository.save(updateActivity);
    }

    Activity addActivity(Activity activity){
       return this.activityRepository.save(activity);
    }


    @Override
    public Activity getActivityById(UUID id) {
       return this.activityRepository.findById(id).
               orElseThrow(()-> new ActivityNotFoundExecption("This activity does not exist"));
}
    @Override
    public List<ActivityListDto> getActivties() {
        return this.activityRepository.findAll().stream().map(activity->modelMapper.map(activity, ActivityListDto.class))
                .collect(Collectors.toList());
    }
}


