package com.ntnu.gidd.service;

import com.ntnu.gidd.dto.ActivityDto;
import com.ntnu.gidd.dto.ActivityListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ActivityService {
    ActivityDto updateActivity(UUID id, ActivityDto activity);
    ActivityDto getActivityById(UUID id);
    ActivityDto saveActivity(ActivityDto activity);
    void deleteActivity(UUID id);
    Page<ActivityListDto> getActivities(Pageable pageable);
    Page<ActivityListDto> findActivitiesWithinRadius(Pageable pageable, GeoLocation position, User user);


}

