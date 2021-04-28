package com.ntnu.gidd.service.ActivityImage;

import com.ntnu.gidd.dto.ActivityImageDto;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.ActivityImage;
import com.ntnu.gidd.repository.ActivityImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ActivityImageServiceImpl implements ActivityImageService {
    @Autowired
    private ActivityImageRepository activityImageRepository;

    ModelMapper modelMapper = new ModelMapper();

    public List<ActivityImage> saveActivityImage(List<ActivityImage> images, Activity activity){
        images.forEach(s -> s.setActivityId(activity.getId()));
       return activityImageRepository.saveAll(images);
    }

    public  List<ActivityImage> updateActivityImage(List<ActivityImageDto> images, Activity activity) {
        activityImageRepository.deleteActivityImageByActivityId(activity.getId());
        activityImageRepository.flush();
        List<ActivityImage> imageList = images.stream().map(s -> modelMapper.map(s, ActivityImage.class)).collect(Collectors.toList());
        imageList.forEach(s -> {
            s.setActivityId(activity.getId());
            s.setId(UUID.randomUUID());
        });
        return  activityImageRepository.saveAll(imageList);
    }
}
