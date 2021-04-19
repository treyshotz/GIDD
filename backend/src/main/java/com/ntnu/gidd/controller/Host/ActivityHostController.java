package com.ntnu.gidd.controller.Host;

import com.ntnu.gidd.dto.ActivityListDto;
import com.ntnu.gidd.dto.UserEmailDto;
import com.ntnu.gidd.dto.UserListDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.exception.UserNotFoundExecption;
import com.ntnu.gidd.service.Host.HostService;
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
@RequestMapping("activities/{activityId}/hosts/")
public class ActivityHostController {

    @Autowired
    private HostService hostService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserListDto> get(@PathVariable UUID activityId){
        try {
            return hostService.getByActivityId(activityId);
        }catch (ActivityNotFoundExecption ex){
            log.debug("[X] Request to get Activities with id={}", activityId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserListDto> update(@PathVariable UUID activityId, @RequestBody UserEmailDto user){
        try {
            log.debug("[X] Request to create host on activity with id={}", activityId);
            return hostService.addHosts(activityId, user);

        }catch (ActivityNotFoundExecption ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("{userId}/")
    @ResponseStatus(HttpStatus.OK)
    public List<UserListDto>  delete(@PathVariable UUID activityId, @PathVariable UUID userId){
        try {
            log.debug("[X] Request to deleted host on activity with id={}", activityId);
            return hostService.deleteHost(activityId, userId);
        }catch (UserNotFoundExecption | ActivityNotFoundExecption ex){
            log.debug("[X] Request to activities/activityId={}/hosts/ resultet in User or Activity not found", activityId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }


}
