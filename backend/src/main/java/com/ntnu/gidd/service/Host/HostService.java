package com.ntnu.gidd.service.Host;

import com.ntnu.gidd.dto.ActivityDto;
import com.ntnu.gidd.dto.ActivityListDto;
import com.ntnu.gidd.dto.UserEmailDto;
import com.ntnu.gidd.dto.UserListDto;

import java.util.List;
import java.util.UUID;

public interface HostService {
    List<ActivityListDto> getAll(UUID userId);
    ActivityDto getActivityFromUser(UUID userId, UUID activityId);
    List<UserListDto> getByActivityId(UUID id);
    List<UserListDto>  addHosts(UUID activityId, UserEmailDto users);
    List<UserListDto>  deleteHost(UUID activityId, UUID userId);
    List<ActivityListDto>  deleteHostfromUser(UUID activityId, UUID userId);



}
