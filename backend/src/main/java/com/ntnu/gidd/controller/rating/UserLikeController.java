package com.ntnu.gidd.controller.rating;

import com.ntnu.gidd.dto.Activity.ActivityListDto;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.service.activity.ActivityService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("users/me/activity-likes/")
public class UserLikeController {

    @Autowired
    ActivityService activityService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ActivityListDto> getLikedActivities(@QuerydslPredicate(root = Activity.class) Predicate predicate,
                                                    @PageableDefault(size = Constants.PAGINATION_SIZE, sort="startDate", direction = Sort.Direction.ASC) Pageable pageable,
                                                    Authentication authentication){
        UserDetails userDetails = (authentication!=null)? (UserDetails) authentication.getPrincipal() : null;
        String email = (userDetails != null) ? userDetails.getUsername() : "";
        return activityService.getLikedActivities(predicate,pageable,email);
    }



}
