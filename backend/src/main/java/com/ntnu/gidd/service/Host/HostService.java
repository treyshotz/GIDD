package com.ntnu.gidd.service.Host;

import com.ntnu.gidd.dto.ActivityHostDto;
import com.ntnu.gidd.dto.ActivityListDto;
import com.ntnu.gidd.dto.UserEmailDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface HostService {
    List<ActivityListDto> getAll(UUID userId);
    ActivityHostDto getById(UUID id);
    ActivityHostDto addHosts(UUID activityId, UserEmailDto users);
    void deleteHost(UUID activityId, UUID userId);


}
