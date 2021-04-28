package com.ntnu.gidd.service.rating;

import com.ntnu.gidd.dto.Activity.ActivityListDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.exception.UserNotFoundException;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@AllArgsConstructor
public class ActivityLikeServiceImpl  implements ActivityLikeService{

    ActivityRepository activityRepository;

    UserRepository userRepository;

    @Override
    public boolean hasLiked(String email, UUID ActivityId) {
        Activity activity = activityRepository.findById(ActivityId).orElse(null);
        User user = userRepository.findByEmail(email).orElse(null);
        if(user != null && activity !=null){
            return activity.getLikes().contains(user);
        }
        return false;
    }

    @Override
    public boolean hasLiked(UUID userId, UUID ActivityId) {
        Activity activity = activityRepository.findById(ActivityId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if(user != null && activity !=null){
            return activity.getLikes().contains(user);
        }
        return false;
    }

    public boolean addLike(String email, UUID ActivityId){
        Activity activity = activityRepository.findById(ActivityId).orElseThrow(ActivityNotFoundExecption::new);
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        if(user != null && activity !=null){
            if(activity.getLikes().contains(user))return true;
            List<User> likes = activity.getLikes();
            likes.add(user);
            activity.setLikes(likes);
            activityRepository.save(activity);
            return true;
        }
        return false;
    }

    public boolean removeLike(String email, UUID ActivityId){
        Activity activity = activityRepository.findById(ActivityId).orElseThrow(ActivityNotFoundExecption::new);
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        if(user != null && activity !=null){
            List<User> likes = activity.getLikes();
            likes.remove(user);
            activity.setLikes(likes);
            activityRepository.save(activity);
            return false;
        }
        return true;
    }

    public Page<ActivityListDto> checkListLikes(Page<ActivityListDto> activities, String email){
        activities.forEach(s -> s.setHasLiked(hasLiked(email, s.getId())));
        return activities;
    }

    @Override
    public Page<ActivityListDto> checkListLikes(Page<ActivityListDto> activities, UUID id) {
        activities.forEach(s -> s.setHasLiked(hasLiked(id, s.getId())));
        return activities;
    }
}
