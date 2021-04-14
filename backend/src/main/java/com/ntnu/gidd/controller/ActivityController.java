package com.ntnu.gidd.controller;

import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @PutMapping("/{activityId}")
    public Activity updateActivity(@PathVariable UUID activityId, @RequestBody Activity activity){
        logger.debug("[X] Request to update Activity with id={}", activityId);
        return this.activityService.updateActivity(activityId, activity);
    }
}
