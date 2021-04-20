package com.ntnu.gidd.service;
import com.ntnu.gidd.dto.ActivityDto;
import com.ntnu.gidd.dto.ActivityListDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.GeoLocation;
import com.ntnu.gidd.model.TrainingLevel;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.TrainingLevelRepository;
import com.ntnu.gidd.service.geolocation.GeoLocationService;
import com.ntnu.gidd.util.TrainingLevelEnum;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygonal;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import lombok.extern.slf4j.Slf4j;
import org.geolatte.geom.GeometryFactory;
import org.geolatte.geom.Point;
import org.geolatte.geom.PointSequence;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {
    ModelMapper modelMapper = new ModelMapper();
    private final GeometryFactory geometryFactory = new GeometryFactory();
    private final GeometricShapeFactory geometricShapeFactory = new GeometricShapeFactory();
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TrainingLevelRepository trainingLevelRepository;

    @Autowired
    private GeoLocationService geoLocationService;

    @Override
    public ActivityDto updateActivity(UUID activityId, ActivityDto activity) {
        Activity updateActivity = this.activityRepository.findById(activityId)
                .orElseThrow(ActivityNotFoundExecption::new);
        updateActivity.setTitle(activity.getTitle());
        updateActivity.setDescription(activity.getDescription());
        updateActivity.setStartDate(activity.getStartDate());
        updateActivity.setEndDate(activity.getEndDate());
        updateActivity.setSignupStart(activity.getSignupStart());
        updateActivity.setSignupEnd(activity.getSignupEnd());
        updateActivity.setClosed(activity.isClosed());
        updateActivity.setCapacity(activity.getCapacity());
        if (activity.getLevel()!=null) updateActivity.setTrainingLevel(getTrainingLevel(activity.getLevel()));
        if (activity.getGeoLocation()!=null) updateActivity.setGeoLocation(activity.getGeoLocation());
        return modelMapper.map(this.activityRepository.save(updateActivity),ActivityDto.class);
    }

    private TrainingLevel getTrainingLevel(TrainingLevelEnum level){
        return trainingLevelRepository.findTrainingLevelByLevel(level).
                orElseThrow(() -> new EntityNotFoundException("Traning level does not exist"));
    }

    @Override
    public ActivityDto getActivityById(UUID id) {
       return modelMapper.map(this.activityRepository.findById(id).
               orElseThrow(ActivityNotFoundExecption::new), ActivityDto.class);
}
    @Override
    public Page<ActivityListDto> getActivities(Pageable pageable) {
        return this.activityRepository.findAll(pageable).map(s -> modelMapper.map(s, ActivityListDto.class));
    }

    @Override
    public ActivityDto saveActivity(ActivityDto activity) {
        Activity newActivity = modelMapper.map(activity, Activity.class);
        if(activity.getLevel()!= null)newActivity.setTrainingLevel(getTrainingLevel(activity.getLevel()));
        if(activity.getGeoLocation() != null) newActivity.setGeoLocation(activity.getGeoLocation());
        return modelMapper.map(this.activityRepository.save(newActivity), ActivityDto.class);
    }

    @Override
    public void deleteActivity(UUID id){
        this.activityRepository.deleteById(id);
    }

    @Override
    public Page<ActivityListDto> findActivitiesWithinRadius(Pageable pageable, GeoLocation position, User user) {
        Point p = geometryFactory.createPoint((PointSequence) new Coordinate(position.getLatitude(), position.getLatitude()));
        return null;
    }

    private Polygonal createCircle(GeoLocation geo, int radius){
        geometricShapeFactory.setNumPoints(12);
        geometricShapeFactory.setCentre(new Coordinate(geo.getLatitude(), geo.getLongitude()));
        geometricShapeFactory.setSize(radius * 2);
        return geometricShapeFactory.createCircle();
    }
}


