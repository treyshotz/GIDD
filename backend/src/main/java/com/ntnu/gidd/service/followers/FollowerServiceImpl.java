package com.ntnu.gidd.service.followers;

import com.ntnu.gidd.dto.User.UserDto;
import com.ntnu.gidd.dto.followers.FollowRequest;
import com.ntnu.gidd.exception.InvalidFollowRequestException;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.service.User.UserService;
import com.ntnu.gidd.util.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class FollowerServiceImpl implements FollowerService {

    private UserService userService;

    private UserRepository userRepository;

    private ModelMapper modelMapper;

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

    @Override
    public Page<UserDto> getFollowingFor(UUID id, Pageable pageable) {
        log.debug("[X] Retrieving all users this user is following (id:{})", id);
        User subject = userService.getUserById(id);

        return userRepository.findByFollowersId(subject.getId(), pageable)
                .map(user -> modelMapper.map(user, UserDto.class));
    }

    @Override
    public Page<UserDto> getFollowersOf(UUID id, Pageable pageable) {
        log.debug("[X] Retrieving followers of user with id {}", id);
        User subject = userService.getUserById(id);

        return userRepository.findByFollowingId(subject.getId(), pageable)
                .map(user -> modelMapper.map(user, UserDto.class));
    }

    @Override
    public Response unfollowUser(UUID actorId, UUID subjectId) {
        log.debug("[X] Unfollowing user with id:{}, actor:{}", subjectId, actorId);
        User actor = userService.getUserById(actorId);
        User subject = userService.getUserById(subjectId);

        actor.removeFollowing(subject);

        log.debug("[X] Successfully followed user (actor:{}, subject:{}", actor.getId(), subject.getId());
        return new Response("Successfully unfollowed user");
    }
}
