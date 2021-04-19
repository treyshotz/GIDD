package com.ntnu.gidd.service;


import com.ntnu.gidd.dto.UserEmailDto;
import com.ntnu.gidd.dto.UserListDto;
import com.ntnu.gidd.factories.ActivityFactory;
import com.ntnu.gidd.factories.UserFactory;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.service.Host.HostService;
import com.ntnu.gidd.service.Host.HostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class HostServiceImplTest {

    @InjectMocks
    private HostServiceImpl hostService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ActivityRepository activityRepository;

    private ActivityFactory activityFactory = new ActivityFactory();
    private UserFactory userFactory = new UserFactory();

    private User user;
    private Activity activity;


    @BeforeEach
    public void setUp() throws Exception {
        activity = activityFactory.getObject();
        assert activity != null;
        activity.setHosts(List.of(Objects.requireNonNull(userFactory.getObject()), userFactory.getObject()));
        lenient().when(activityRepository.save(activity)).thenReturn(activity);

        user = userFactory.getObject();
        assert user != null;
        user.setActivities(List.of(Objects.requireNonNull(activityFactory.getObject()), activityFactory.getObject()));
        lenient().when(userRepository.save(user)).thenReturn(user);

        lenient().when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        lenient().when(activityRepository.findById(activity.getId())).thenReturn(Optional.ofNullable(activity));
    }

    @Test
    void testGetAllReturnAListOfActivities(){
        assertThat(hostService.getAll(user.getId())).isInstanceOf(List.class);
        assertThat(hostService.getAll(user.getId()).get(0).getTitle())
                .isEqualTo(user.getActivities().get(0).getTitle());
    }

    @Test
    void testGetActivityFromUserReturnActivity(){
        lenient().when(activityRepository.findActivityByIdAndHosts_Id(user.getActivities().get(0).getId(),user.getId())).
                thenReturn(Optional.ofNullable(user.getActivities().get(0)));
        assertThat(hostService.getActivityFromUser(user.getId(), user.getActivities().get(0).getId()).getTitle())
                .isEqualTo( user.getActivities().get(0).getTitle());

    }

    @Test
    void testGetActivityByIdReturnListOfUsers(){
        assertThat(hostService.getByActivityId(activity.getId())).isInstanceOf(List.class);
        assertThat(hostService.getByActivityId(activity.getId()).get(0).getSurname())
                .isEqualTo(activity.getHosts().get(0).getSurname());

    }

    @Test
    void testaddHostReturnListOfUpdatedUsers(){
        UserEmailDto email = UserEmailDto.builder().email(user.getEmail()).build();
        lenient().when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));
        assertThat(hostService.addHosts(activity.getId(),email)).isInstanceOf(List.class);
        assertThat(hostService.addHosts(activity.getId(),email).get(2).getEmail()).isEqualTo(email.getEmail());
    }

    @Test
    void testDeleteHostReturnListOfUpdatedUsers() throws Exception {
        User deleteUser = userFactory.getObject();
        ArrayList<User> list = new ArrayList<>(activity.getHosts());
        list.add(deleteUser);
        activity.setHosts(list);
        assert deleteUser != null;
        assertThat(activity.getHosts().size()).isEqualTo(3);
        lenient().when(userRepository.findById(deleteUser.getId()))
                .thenReturn(Optional.of(deleteUser));

        assertThat(hostService.deleteHost(activity.getId(),deleteUser.getId()))
                .isInstanceOf(List.class);
        assertThat(hostService.deleteHost(activity.getId(),deleteUser.getId()).size()).isEqualTo(2);
    }

    @Test
    void testDeleteHostFromUserReturnListOfUpdatedActivities() throws Exception {
        Activity deleteActivity = activityFactory.getObject();
        ArrayList<Activity> list = new ArrayList<>(user.getActivities());
        list.add(deleteActivity);
        user.setActivities(list);
        assert deleteActivity != null;
        assertThat(user.getActivities().size()).isEqualTo(3);
        lenient().when(activityRepository.findById(deleteActivity.getId()))
                .thenReturn(Optional.of(deleteActivity));

        assertThat(hostService.deleteHostfromUser(deleteActivity.getId(),user.getId()))
                .isInstanceOf(List.class);
        assertThat(hostService.deleteHostfromUser(deleteActivity.getId(),user.getId()).size()).isEqualTo(2);
    }
}
