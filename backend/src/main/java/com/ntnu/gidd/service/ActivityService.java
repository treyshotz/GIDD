package com.ntnu.gidd.service;

import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ActivityService {

    Activity updateActivity(UUID id, Activity activity);
}
