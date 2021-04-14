package com.ntnu.gidd.service;

import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.repository.ActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public Activity updateActivity(UUID activityId, Activity activity) {
        Activity updateActivity = this.activityRepository.getOne(activityId);

        updateActivity.setTitle(activity.getTitle());
        updateActivity.setDescription(activity.getDescription());
        updateActivity.setStart_date(activity.getStart_date());
        updateActivity.setEnd_date(activity.getEnd_date());
        updateActivity.setSignup_start(activity.getSignup_start());
        updateActivity.setSignup_end(activity.getSignup_end());
        updateActivity.setClosed(activity.isClosed());
        updateActivity.setCapacity(updateActivity.getCapacity());

        return this.activityRepository.save(updateActivity);
    }
}
