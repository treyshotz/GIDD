package com.ntnu.gidd.service;

import com.ntnu.gidd.dto.ActivityDto;
import com.ntnu.gidd.dto.ActivityListDto;
import com.ntnu.gidd.model.Activity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface ActivityService {
    ActivityDto updateActivity(UUID id, Activity activity);
    List<ActivityListDto> getActivties();
    ActivityDto getActivityById(UUID id);
    ActivityDto saveActivity(Activity activity);
    void deleteActivity(UUID id);

}

