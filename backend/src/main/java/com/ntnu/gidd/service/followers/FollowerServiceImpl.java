package com.ntnu.gidd.service.followers;

import com.ntnu.gidd.dto.followers.FollowRequest;
import com.ntnu.gidd.exception.InvalidFollowRequestException;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.service.User.UserService;
import com.ntnu.gidd.util.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class FollowerServiceImpl implements FollowerService {

    private UserService userService;

    @Override
    public Response registerFollow(FollowRequest followRequest) {
        log.debug("[X] Registering request to follow: {}", followRequest);

        if (followRequest.isIdentical())
            throw new InvalidFollowRequestException("You cannot follow yourself");

        User actor = userService.getUserById(followRequest.getActorId());
        User subject = userService.getUserById(followRequest.getSubjectId());

        actor.addFollowing(subject);

        log.debug("[X] Successfully followed user (actor:{}, subject:{}", actor.getId(), subject.getId());
        return new Response("Successfully followed user");
    }
}
