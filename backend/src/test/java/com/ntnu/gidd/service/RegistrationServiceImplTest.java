package com.ntnu.gidd.service;

import com.ntnu.gidd.factories.ActivityFactory;
import com.ntnu.gidd.factories.RegistrationFactory;
import com.ntnu.gidd.factories.UserFactory;
import com.ntnu.gidd.model.Activity;
import com.ntnu.gidd.model.Registration;
import com.ntnu.gidd.model.RegistrationId;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.RegistrationRepository;
import com.ntnu.gidd.repository.TrainingLevelRepository;
import com.ntnu.gidd.repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceImplTest {

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Mock
    private RegistrationRepository registrationRepository;

    private Registration registration;
    private Registration registration2;
    private List<Registration> registrationsExpected;

    @BeforeEach
    public void setUp() throws Exception {

        registration = new RegistrationFactory().getObject();
        registration2 = new RegistrationFactory().getObject();

        assert registration != null;
        assert registration2 != null;

        when(registrationRepository.save(registration)).thenReturn(registration);
        when(registrationRepository.save(registration2)).thenReturn(registration2);
        registrationService.saveRegistration(registration);
        registrationService.saveRegistration(registration2);
    }

    @AfterEach
    public void cleanUp(){
        registrationRepository.delete(registration);
        registrationRepository.delete(registration2);
    }

    @Test
    void testRegistrationServiceImplGetRegistrationForActivityReturnsRegistration(){
        registrationsExpected = List.of(registration);
        when(registrationRepository.findRegistrationsByActivity_Id(registration.getActivity().getId())).thenReturn(Optional.of(registrationsExpected));
        List<Registration> registrationsFound = registrationService.getRegistrationForActivity(registration.getActivity().getId());

        for (int i = 0; i < registrationsFound.size(); i++) {
            assertThat(registrationsFound.get(i).getRegistrationId()).isEqualTo(registrationsExpected.get(i).getRegistrationId());
            assertThat(registrationsFound.get(i)).isNotNull();
        }
    }

    @Test
    void testRegistrationServiceImplGetRegistrationForUserReturnsRegistration(){
        registrationsExpected = List.of(registration, registration2);
        when(registrationRepository.findRegistrationsByUser_Id(registration.getUser().getId())).thenReturn(Optional.of(registrationsExpected));
        List<Registration> registrationsFound = registrationService.getRegistrationsForUser(registration.getUser().getId());

        for (int i = 0; i < registrationsExpected.size(); i++) {
            assertThat(registrationsFound.get(i).getRegistrationId()).isEqualTo(registrationsExpected.get(i).getRegistrationId());
            assertThat(registrationsFound.get(i)).isNotNull();
        }
    }
    
    @Test
    void testRegistrationServiceImplGetRegistrationWithRegistrationId(){
        when(registrationRepository.findById(registration.getRegistrationId())).thenReturn(Optional.ofNullable(registration));
        Registration registrationFound = registrationService.getRegistrationWithRegistrationId(registration.getRegistrationId());

        assertThat(registrationFound.getRegistrationId()).isEqualTo(registration.getRegistrationId());
        assertThat(registrationFound).isNotNull();
    }

    @Test
    void testRegistrationServiceImplDeleteRegistrationWithCompositeId() {
        registrationsExpected = List.of(registration2);

        lenient().when(registrationRepository.findRegistrationByUser_IdAndActivity_Id(registration.getUser().getId(), registration.getActivity().getId())).thenReturn(Optional.ofNullable(registration));
        doNothing().when(registrationRepository).deleteRegistrationsByUser_IdAndActivity_Id(registration.getUser().getId(), registration.getActivity().getId());

        registrationService.deleteRegistrationWithCompositeId(registration.getUser().getId(), registration.getActivity().getId());

        when(registrationRepository.findAll()).thenReturn(registrationsExpected);
        List<Registration> registrationsFound = registrationRepository.findAll();

        for (int i = 0; i < registrationsExpected.size(); i++){
            assertThat(registrationsFound.get(i).getRegistrationId()).isEqualTo(registrationsExpected.get(i).getRegistrationId());
        }
    }
}
