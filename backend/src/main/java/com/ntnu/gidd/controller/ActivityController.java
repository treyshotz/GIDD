package com.ntnu.gidd.controller;

import com.ntnu.gidd.dto.ActivityDto;
import com.ntnu.gidd.dto.ActivityListDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.service.ActivityService;
import com.ntnu.gidd.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("activities/")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ActivityListDto> getAll(){
        return activityService.getActivties();
    }
    
    @GetMapping("{activityId}/")
    @ResponseStatus(HttpStatus.OK)
    public ActivityDto get(@PathVariable UUID activityId){
        try {
            return activityService.getActivityById(activityId);
        }catch (ActivityNotFoundExecption ex){
            log.debug("[X] Request to update Activity with id={}", activityId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PutMapping("{activityId}/")
    @ResponseStatus(HttpStatus.OK)
    public ActivityDto updateActivity(@PathVariable UUID activityId, @RequestBody ActivityDto activity){
        try {
        log.debug("[X] Request to update Activity with id={}", activityId);
        return this.activityService.updateActivity(activityId, activity);
    } catch (ActivityNotFoundExecption ex){
        log.debug("[X] Request to update Activity with id={}", activityId);
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, ex.getMessage(), ex);
    }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActivityDto postActivity(@RequestBody Activity activity){
        return activityService.saveActivity(activity);
    }

    @DeleteMapping("/{activityId}/")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteActivity(@PathVariable UUID activityId){
        log.debug("[X] Request to delete Activity with id={}", activityId);
        activityService.deleteActivity(activityId);
        return new Response( "Activity has been deleted");
    }

}
