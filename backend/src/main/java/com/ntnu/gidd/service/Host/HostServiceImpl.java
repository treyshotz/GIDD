package com.ntnu.gidd.service.Host;

import com.ntnu.gidd.dto.ActivityDto;
import com.ntnu.gidd.dto.ActivityListDto;
import com.ntnu.gidd.dto.UserEmailDto;
import com.ntnu.gidd.dto.UserListDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.exception.UserNotFoundException;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.QActivity;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.UserRepository;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;



@Slf4j
@Service
public class HostServiceImpl implements HostService {

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    UserRepository userRepository;

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public Page<ActivityListDto> getAll(Predicate predicate, Pageable pageable, UUID userId) {
        QActivity activity = QActivity.activity;
        predicate = ExpressionUtils.allOf(predicate, activity.hosts.any().id.eq(userId));
        return activityRepository.findAll(predicate, pageable).map(a-> modelMapper.map(a,ActivityListDto.class));

    }
    
    @Override
    public Page<ActivityListDto> getAllByEmail(Predicate predicate, Pageable pageable, String email) {
        QActivity activity = QActivity.activity;
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        predicate = ExpressionUtils.allOf(predicate, activity.hosts.any().id.eq(user.getId()).or(activity.creator.id.eq(user.getId())));
        return activityRepository.findAll(predicate, pageable).map(a -> modelMapper.map(a, ActivityListDto.class));
    }
    
    @Override
    public ActivityDto getActivityFromUser(UUID userId, UUID activityId) {
        return modelMapper.map(activityRepository.findActivityByIdAndHosts_Id(activityId, userId)
                .orElseThrow(ActivityNotFoundExecption::new), ActivityDto.class);

    }
    
    @Override
    public ActivityDto getActivityFromUserByEmail(String email, UUID activityId) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(activityRepository.findActivityByIdAndHosts_Id(activityId, user.getId())
                .orElseThrow(ActivityNotFoundExecption::new), ActivityDto.class);
        
    }

    @Override
    public List<UserListDto> getByActivityId(UUID id) {
        return activityRepository.findById(id).orElseThrow(ActivityNotFoundExecption::new)
                .getHosts().stream().map(a -> modelMapper.map(a, UserListDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<UserListDto>  addHosts(UUID activityId, UserEmailDto user) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(ActivityNotFoundExecption::new);
        ArrayList<User> list = new ArrayList<>(activity.getHosts()) ;
        list.add(userRepository.findByEmail(user.getEmail()).
                orElseThrow(UserNotFoundException::new));
        activity.setHosts(list);
        return activityRepository.save(activity)
                .getHosts().stream()
                .map(a -> modelMapper.map(a, UserListDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserListDto> deleteHost(UUID activityId, UUID userId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(ActivityNotFoundExecption::new);
        ArrayList<User> list = new ArrayList<>(activity.getHosts()) ;
       list.remove(userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new));
       activity.setHosts(list);
        return activityRepository.save(activity).getHosts().stream()
                .map(a -> modelMapper.map(a, UserListDto.class))
                .collect(Collectors.toList());

    }

    @Override
    public List<ActivityListDto> deleteHostfromUser(UUID activityId, UUID userId) {
       User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        ArrayList<Activity> list = new ArrayList<>(user.getActivities());
        list.remove(activityRepository.findById(activityId).orElseThrow(ActivityNotFoundExecption::new));
        user.setActivities(list);
       return userRepository.save(user).getActivities().stream()
               .map(a -> modelMapper.map(a, ActivityListDto.class))
               .collect(Collectors.toList());
    }
    
    @Override
    public List<ActivityListDto> deleteHostfromUserByEmail(UUID activityId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        ArrayList<Activity> list = new ArrayList<>(user.getActivities());
        list.remove(activityRepository.findById(activityId).orElseThrow(ActivityNotFoundExecption::new));
        user.setActivities(list);
        return userRepository.save(user).getActivities().stream()
                .map(a -> modelMapper.map(a, ActivityListDto.class))
                .collect(Collectors.toList());
    }
}
