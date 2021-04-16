package com.ntnu.gidd.controller.Host;

import com.ntnu.gidd.dto.ActivityHostDto;
import com.ntnu.gidd.dto.ActivityListDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.exception.UserNotFoundExecption;
import com.ntnu.gidd.service.Host.HostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("user/{userId}/hosts/")
public class UserHostController {

    @Autowired
    private HostService hostService;



    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ActivityListDto> getAll(@PathVariable UUID userId){
        try {
            log.debug("[X] Request to get all Activities of user with userId={}", userId);
            return hostService.getAll(userId);
        }catch (UserNotFoundExecption ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }

    }

    @GetMapping("{activityId}/")
    @ResponseStatus(HttpStatus.OK)
    public ActivityHostDto get(@PathVariable UUID userId, @PathVariable UUID activityId){
        try {
            return hostService.getById(activityId);
        }catch (ActivityNotFoundExecption ex){
            log.debug("[X] Request to get Activity of user with userId={} and activityId={}", userId,activityId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }
}
