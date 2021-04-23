package com.ntnu.gidd.controller.activity;



import com.ntnu.gidd.dto.Activity.ActivityDto;
import com.ntnu.gidd.dto.Activity.ActivityListDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.GeoLocation;
import com.ntnu.gidd.model.GeoLocationId;
import com.ntnu.gidd.service.Activity.ActivityService;
import com.ntnu.gidd.util.Constants;
import com.ntnu.gidd.util.Response;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("activities/")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ActivityListDto> getAll(@QuerydslPredicate(root = Activity.class) Predicate predicate,
                                        @PageableDefault(size = Constants.PAGINATION_SIZE, sort="startDate", direction = Sort.Direction.ASC) Pageable pageable,
                                        @RequestParam(required = false) Double range,
                                        @RequestParam(required = false) Double lat,
                                        @RequestParam(required = false) Double lng){

            if (range != null && lat != null && lng != null) {
                GeoLocation position = new GeoLocation(lat, lng);
                return activityService.getActivities(predicate, pageable, position, range);
            }
            return activityService.getActivities(predicate, pageable);
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
    public ActivityDto postActivity(Authentication authentication, @RequestBody ActivityDto activity){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return activityService.saveActivity(activity, userDetails.getUsername());
    }

    @DeleteMapping("/{activityId}/")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteActivity(@PathVariable UUID activityId){
        log.debug("[X] Request to delete Activity with id={}", activityId);
        activityService.deleteActivity(activityId);
        return new Response( "Activity has been deleted");
    }

}
