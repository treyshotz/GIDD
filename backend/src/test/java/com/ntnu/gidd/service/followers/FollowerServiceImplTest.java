package com.ntnu.gidd.service.followers;

import com.ntnu.gidd.dto.followers.FollowRequest;
import com.ntnu.gidd.exception.InvalidFollowRequestException;
import com.ntnu.gidd.exception.UserNotFoundException;
import com.ntnu.gidd.factories.UserFactory;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.service.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowerServiceImplTest {

    @InjectMocks
    private FollowerServiceImpl followerService;

    @Mock
    private UserService userService;

    private UserFactory userFactory = new UserFactory();

    private User actor;

    private User subject;

    private FollowRequest followRequest;

    @BeforeEach
    void setUp() throws Exception {
        actor = userFactory.getObject();
        subject = userFactory.getObject();
        followRequest = new FollowRequest(actor.getId(), subject.getId());

        lenient().when(userService.getUserById(actor.getId())).thenReturn(actor);
        lenient().when(userService.getUserById(subject.getId())).thenReturn(subject);
    }

    @Test
    public void testRegisterFollowWhenActorAndSubjectAreIdenticalThrowsError() {
        followRequest.setSubjectId(actor.getId());

        assertThatExceptionOfType(InvalidFollowRequestException.class)
                .isThrownBy(() -> followerService.registerFollow(followRequest));
    }

    @Test
    public void testRegisterFollowWhenValidRequestAddsSubjectToActorsFollowing() {
        followerService.registerFollow(followRequest);

        assertThat(actor.getFollowing()).contains(subject);
    }

    @Test
    public void testRegisterFollowWhenValidRequestAddsActorToSubjectsFollowers() {
        followerService.registerFollow(followRequest);

        assertThat(subject.getFollowers()).contains(actor);
    }

    @Test
    public void testRegisterFollowWhenActorNotFoundThrowsError() {
        when(userService.getUserById(actor.getId())).thenThrow(UserNotFoundException.class);

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> followerService.registerFollow(followRequest));
    }

    @Test
    public void testRegisterFollowWhenSubjectNotFoundThrowsError() {
        when(userService.getUserById(subject.getId())).thenThrow(UserNotFoundException.class);

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> followerService.registerFollow(followRequest));
    }

}