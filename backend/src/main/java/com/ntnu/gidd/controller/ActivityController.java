package com.ntnu.gidd.controller;

import com.ntnu.gidd.dto.ActivityListDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.service.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("activities/")
public class ActivityController {

    private ActivityService activityService;

    Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ActivityListDto> getAll(){
        return activityService.getActivties();
    }

    @GetMapping("{activityId}/")
    @ResponseStatus(HttpStatus.OK)
    public Activity get(@PathVariable UUID activityId){
        try {
            return activityService.getActivityById(activityId);
        }catch (ActivityNotFoundExecption ex){
            logger.debug("[X] Request to update Activity with id={}", activityId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PutMapping("{activityId}/")
    public Activity updateActivity(@PathVariable UUID activityId, @RequestBody Activity activity){
        logger.debug("[X] Request to update Activity with id={}", activityId);
        return this.activityService.updateActivity(activityId, activity);
    }
}
