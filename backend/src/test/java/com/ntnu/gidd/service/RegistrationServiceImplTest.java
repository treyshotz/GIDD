package com.ntnu.gidd.service;

import com.ntnu.gidd.factories.ActivityFactory;
import com.ntnu.gidd.factories.UserFactory;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.Registration;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.RegistrationRepository;
import com.ntnu.gidd.repository.TrainingLevelRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceImplTest {

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Mock
    private RegistrationRepository registrationRepository;

    private User user;
    private Activity activity;
    private Activity activity2;
    private Registration registration;
    private Registration registration2;
    private List<Registration> registrationsExpected;

    @BeforeEach
    public void setUp() throws Exception {
        user = new UserFactory().getObject();
        activity = new ActivityFactory().getObject();
        activity2 = new ActivityFactory().getObject();
        registration = new Registration();
        registration.setUser(user);
        registration.setActivity(activity);
        registration2 = new Registration();
        registration2.setUser(user);
        registration2.setActivity(activity2);

        assert activity != null;
        assert user != null;
        assert registration != null;

        when(registrationRepository.save(registration)).thenReturn(registration);
        registrationService.saveRegistration(registration);
    }

    @AfterEach
    public void cleanUp(){

    }

    @Test
    void testRegistrationServiceImplGetRegistrationForActivityReturnsRegistration(){
        registrationsExpected = List.of(registration);
        when(registrationRepository.findRegistrationsByActivity_Id(activity.getId())).thenReturn(Optional.of(registrationsExpected));
        List<Registration> registrationsFound = registrationService.getRegistrationForActivity(activity.getId());

        for (int i = 0; i < registrationsFound.size(); i++) {
            assertThat(registrationsFound.get(i).getRegistrationId()).isEqualTo(registrationsExpected.get(i).getRegistrationId());
        }
    }

    @Test
    void testRegistrationServiceImplGetRegistrationForUserReturnsRegistration(){
        registrationsExpected = List.of(registration, registration2);
        when(registrationRepository.findRegistrationsByUser_Id(user.getId())).thenReturn(Optional.of(registrationsExpected));
        List<Registration> registrationsFound = registrationService.getRegistrationsForUser(user.getId());

        for (int i = 0; i < registrationsFound.size(); i++) {
            assertThat(registrationsFound.get(i).getRegistrationId()).isEqualTo(registrationsExpected.get(i).getRegistrationId());
        }
    }
    
    @Test
    void testRegistrationServiceImplGetRegistrationWithRegistrationId(){
        when(registrationRepository.findById(registration.getRegistrationId())).thenReturn(Optional.ofNullable(registration));
        Registration registrationFound = registrationService.getRegistrationWithRegistrationId(registration.getRegistrationId());
        assertThat(registrationFound.getRegistrationId()).isEqualTo(registration.getRegistrationId());
    }
}
