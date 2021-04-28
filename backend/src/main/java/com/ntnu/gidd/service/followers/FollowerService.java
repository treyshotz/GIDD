package com.ntnu.gidd.service.followers;

import com.ntnu.gidd.dto.followers.FollowRequest;
import com.ntnu.gidd.util.Response;

public interface FollowerService {

     Response registerFollow(FollowRequest followRequest);
}
