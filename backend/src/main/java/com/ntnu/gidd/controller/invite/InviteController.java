package com.ntnu.gidd.controller.invite;

import com.ntnu.gidd.dto.User.UserEmailDto;
import com.ntnu.gidd.dto.User.UserListDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.exception.InvalidUnInviteExecption;
import com.ntnu.gidd.exception.UserNotFoundException;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.service.Host.HostService;
import com.ntnu.gidd.service.invite.InviteService;
import com.ntnu.gidd.util.Constants;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("activities/{activityId}/invites/")
public class InviteController {

    @Autowired
    private InviteService inviteService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<UserListDto> getAll(@QuerydslPredicate(root = User.class) Predicate predicate,
                                 @PageableDefault(size = Constants.PAGINATION_SIZE, sort="firstName", direction = Sort.Direction.DESC) Pageable pageable,
                                 @PathVariable UUID activityId){
        try {
            return inviteService.getAllInvites(predicate, pageable, activityId);
        }catch (ActivityNotFoundExecption ex){
            log.debug("[X] Request to get Activities invites with id={} resulted in activity not found", activityId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<UserListDto> update(@QuerydslPredicate(root = User.class) Predicate predicate,
                                    @PageableDefault(size = Constants.PAGINATION_SIZE, sort="firstName", direction = Sort.Direction.DESC) Pageable pageable,
                                    @PathVariable UUID activityId, @RequestBody UserEmailDto user){
        try {
            log.debug("[X] Request to create host on activity with id={}", activityId);
            return inviteService.inviteUser(predicate, pageable,activityId, user);

        }catch (ActivityNotFoundExecption | UserNotFoundException ex ){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("{userId}/")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserListDto>  delete(@QuerydslPredicate(root = User.class) Predicate predicate,
                                     @PageableDefault(size = Constants.PAGINATION_SIZE, sort="firstName", direction = Sort.Direction.DESC) Pageable pageable,
                                     @PathVariable UUID activityId, @PathVariable UUID userId){
        try {
            log.debug("[X] Request to deleted host on activity with id={}", activityId);
            return inviteService.unInviteUser(predicate, pageable, activityId, userId);
        }catch (UserNotFoundException | ActivityNotFoundExecption ex){
            log.debug("[X] Request to activities/activityId={}/hosts/ resultet in User or Activity not found", activityId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }catch (InvalidUnInviteExecption ex){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}