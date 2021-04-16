package com.ntnu.gidd.service.Host;

import com.ntnu.gidd.dto.ActivityHostDto;
import com.ntnu.gidd.dto.ActivityListDto;
import com.ntnu.gidd.dto.UserEmailDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.exception.UserNotFoundExecption;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<ActivityListDto> getAll(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundExecption::new);
        return activityRepository.findActivitiesByHosts(user).stream()
                .map(activity->modelMapper.map(activity, ActivityListDto.class))
                .collect(Collectors.toList());

    }

    @Override
    public ActivityHostDto getById(UUID id) {
        return modelMapper.map(activityRepository.findById(id)
                        .orElseThrow(ActivityNotFoundExecption::new), ActivityHostDto.class);
    }

    @Override
    public ActivityHostDto addHosts(UUID activityId, UserEmailDto user) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(ActivityNotFoundExecption::new);
        activity.getHosts().add(userRepository.findByEmail(user.getEmail()).
                orElseThrow(UserNotFoundExecption::new));
        return modelMapper.map(activityRepository.save(activity), ActivityHostDto.class);
    }

    @Override
    public void deleteHost(UUID activityId, UUID userId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(ActivityNotFoundExecption::new);

        activity.getHosts().remove(userRepository.findById(userId)
                .orElseThrow(UserNotFoundExecption::new));

    }
}
