package com.ntnu.gidd.service.Activity;

import com.ntnu.gidd.dto.Activity.ActivityDto;
import com.ntnu.gidd.dto.Activity.ActivityListDto;
import com.ntnu.gidd.dto.EquipmentDto;
import com.ntnu.gidd.dto.geolocation.GeoLocationDto;
import com.ntnu.gidd.dto.Registration.RegistrationUserDto;
import com.ntnu.gidd.exception.ActivityNotFoundExecption;
import com.ntnu.gidd.exception.NotInvitedExecption;
import com.ntnu.gidd.exception.UserNotFoundException;
import com.ntnu.gidd.model.*;
import com.ntnu.gidd.model.Activity;

import com.ntnu.gidd.model.HtmlTemplate;
import com.ntnu.gidd.model.Mail;
import com.ntnu.gidd.model.TrainingLevel;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.*;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.TrainingLevelRepository;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.service.Activity.expression.ActivityExpression;
import com.ntnu.gidd.service.ActivityImage.ActivityImageService;
import com.ntnu.gidd.service.Activity.ActivityService;
import com.ntnu.gidd.service.Email.EmailService;
import com.ntnu.gidd.service.Registration.RegistrationService;
import com.ntnu.gidd.service.equipment.EquipmentService;
import com.ntnu.gidd.service.User.UserService;
import com.ntnu.gidd.service.User.UserServiceImpl;
import com.ntnu.gidd.service.Registration.RegistrationService;
import com.ntnu.gidd.service.invite.InviteService;
import com.ntnu.gidd.util.TrainingLevelEnum;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;

import java.util.HashMap;
import java.util.Map;
import javax.mail.MessagingException;

import com.querydsl.jpa.JPAExpressions;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TrainingLevelRepository trainingLevelRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    ActivityImageService activityImageService;

    @Autowired
    EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    InviteService inviteService;


    @Transactional
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
        if(activity.getLevel()!=null)updateActivity.setTrainingLevel(getTrainingLevel(activity.getLevel()));

        if (activity.getGeoLocation() != null)
            setGeoLocation(activity, updateActivity);

        if (activity.getEquipment() != null)
            setEquipment(activity, updateActivity);
         
        if(updateActivity.isClosed()){
          closeActivity(activity);
        }
        if (activity.getImages() !=  null) {
          updateActivity.setImages(activityImageService.updateActivityImage(activity.getImages(), updateActivity));
        }
        if (activity.getImages() !=  null) updateActivity.setImages(activityImageService.updateActivityImage(
                activity.getImages(), updateActivity
        ));

        if(!updateActivity.isInviteOnly() && activity.isInviteOnly()){
            inviteService.inviteBatch(activityId,
                    registrationService.getRegistratedUsersInActivity(activityId));
        }
        updateActivity.setInviteOnly(activity.isInviteOnly());
        if (activity.getImages() !=  null){
            updateActivity.setImages(activityImageService.updateActivityImage(activity.getImages(), updateActivity));

        }

        if(updateActivity.isClosed()){
        closeActivity(activity);

        }
        return modelMapper.map(this.activityRepository.save(updateActivity) ,ActivityDto.class);

    }

    private TrainingLevel getTrainingLevel(TrainingLevelEnum level){
        return trainingLevelRepository.findTrainingLevelByLevel(level).
                orElseThrow(() -> new EntityNotFoundException("Traning level does not exist"));
    }

    public boolean closeActivity(ActivityDto activity ){
        List<RegistrationUserDto> regListDtos = registrationService.getRegistrationForActivity(activity.getId());

        try{
            for(RegistrationUserDto registrationDto : regListDtos){
                Map<String, Object> properties = new HashMap<>();
                properties.put("name", registrationDto.getUser().getFirstName() + " " + registrationDto.getUser().getSurname());
                properties.put("activity", activity.getTitle());
                properties.put("url", "https://gidd-idatt2106.web.app/");

                Mail mail = Mail.builder()
                    .from("baregidd@gmail.com")
                    .to(registrationDto.getUser().getEmail())
                    .htmlTemplate(new HtmlTemplate("activity_closed", properties))
                    .subject("Activity closed")
                    .build();
                emailService.sendEmail(mail);
            }
            return true;
        }
        catch (MessagingException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public ActivityDto getActivityById(UUID id, String email) {
        Activity activity = this.activityRepository.findById(id).
                orElseThrow(ActivityNotFoundExecption::new);
        if(activity.isInviteOnly() && !checkReadAccess(activity, email)){
            throw new NotInvitedExecption();
        }
        return modelMapper.map(activity, ActivityDto.class);

    }

    private boolean checkReadAccess(Activity activity, String email){
        User user = userRepository.findByEmail(email).orElse(null);
        return (activity.getInvites().contains(user) | activity.getCreator().equals(user) | activity.getHosts().contains(user));
    }
    @Override
    public Page<ActivityListDto> getActivities(Predicate predicate, Pageable pageable, String username) {
        QActivity activity = QActivity.activity;
        if(username.equals("")){
             predicate = ExpressionUtils.and(predicate,activity.inviteOnly.isFalse());
        }else {
            predicate = ExpressionUtils.and(predicate,
                    activity.inviteOnly.isFalse().or(
                    activity.invites.any().email.eq(username)
                    .or(activity.creator.email.eq(username))
                    .or(activity.hosts.any().email.eq(username)))

            );
        }

        assert predicate != null;
        return this.activityRepository.findAll(predicate, pageable)
                .map(s -> modelMapper.map(s, ActivityListDto.class));
    }

    @Override
    public Page<ActivityListDto> getActivities(Predicate predicate, Pageable pageable, GeoLocation position, Double range,  String username) {
        
        predicate = ActivityExpression.of(predicate)
                .closestTo(position)
                .range(range)
                .toPredicate();
        return getActivities(predicate, pageable, username);
    }


    @Override
    public ActivityDto saveActivity(ActivityDto activity, String creatorEmail) {
        Activity newActivity = modelMapper.map(activity, Activity.class);
        User user = userRepository.findByEmail(creatorEmail).orElseThrow(UserNotFoundException::new);
        newActivity.setId(UUID.randomUUID());
        newActivity.setCreator(user);
        newActivity.setHosts(List.of());
        if(activity.getLevel()!= null)newActivity.setTrainingLevel(getTrainingLevel(activity.getLevel()));

        if (activity.getGeoLocation() != null)
            setGeoLocation(activity, newActivity);

        if (activity.getEquipment() != null)
            setEquipment(activity, newActivity);

        newActivity  = this.activityRepository.save(newActivity);
        if (activity.getImages() !=  null) newActivity.setImages(activityImageService.saveActivityImage(
                newActivity.getImages(), newActivity
        ));
        return modelMapper.map(newActivity, ActivityDto.class);
    }

    private void setGeoLocation(ActivityDto activity, Activity newActivity) {
        GeoLocationDto geoLocationDto = activity.getGeoLocation();
        GeoLocation geoLocation = new GeoLocation(geoLocationDto
                                                          .getLat(), activity.getGeoLocation()
                .getLng());

        newActivity.setGeoLocation(geoLocation);
    }

    private void setEquipment(ActivityDto activity, Activity newActivity){
        newActivity.setEquipment(equipmentService.saveAndReturnEquipments(activity.getEquipment()));
    }

    @Override
    public void deleteActivity(UUID id){
        this.activityRepository.deleteById(id);
    }
}


