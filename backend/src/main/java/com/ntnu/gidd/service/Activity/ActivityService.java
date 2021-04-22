package com.ntnu.gidd.service.Activity;

import com.ntnu.gidd.dto.Activity.ActivityDto;
import com.ntnu.gidd.dto.Activity.ActivityListDto;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ActivityService {
    ActivityDto updateActivity(UUID id, ActivityDto activity);
    ActivityDto getActivityById(UUID id);
    ActivityDto saveActivity(ActivityDto activity, String creatorEmail);
    void deleteActivity(UUID id);
    Page<ActivityListDto> getActivities(Predicate predicate, Pageable pageable);


}

