package com.ntnu.gidd.controller.rating;

import com.ntnu.gidd.dto.Activity.ActivityListDto;
import com.ntnu.gidd.dto.LikeDto;
import com.ntnu.gidd.dto.User.UserEmailDto;
import com.ntnu.gidd.dto.User.UserListDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.exception.InvalidUnInviteExecption;
import com.ntnu.gidd.exception.UserNotFoundException;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.service.invite.InviteService;
import com.ntnu.gidd.service.rating.ActivityLikeService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("activities/{activityId}/likes/")
public class ActivityLikeController {

    @Autowired
    private ActivityLikeService activityLikeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public LikeDto hasLiked(Authentication authentication, @PathVariable UUID activityId){
        UserDetails userDetails =  (UserDetails) authentication.getPrincipal() ;
        String email = userDetails.getUsername();
        return new LikeDto(activityLikeService.hasLiked(email, activityId));

    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public LikeDto like(Authentication authentication, @PathVariable UUID activityId){
        try{
            UserDetails userDetails =  (UserDetails) authentication.getPrincipal() ;
            String email = userDetails.getUsername();
            return new LikeDto(activityLikeService.addLike(email, activityId));
        }catch (UserNotFoundException | ActivityNotFoundExecption ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage());
        }

    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public LikeDto unLike(Authentication authentication, @PathVariable UUID activityId){
        try{
            UserDetails userDetails =  (UserDetails) authentication.getPrincipal() ;
            String email = userDetails.getUsername();
            return new LikeDto(activityLikeService.removeLike(email, activityId));
        }catch (UserNotFoundException | ActivityNotFoundExecption ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }


}
