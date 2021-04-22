package com.ntnu.gidd.service;

import com.ntnu.gidd.dto.Registration.RegistrationActivityDto;
import com.ntnu.gidd.dto.Registration.RegistrationActivityListDto;
import com.ntnu.gidd.dto.Registration.RegistrationUserDto;
import com.ntnu.gidd.factories.RegistrationFactory;
import com.ntnu.gidd.model.Registration;
import com.ntnu.gidd.repository.ActivityRepository;
import com.ntnu.gidd.repository.RegistrationRepository;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.service.Registration.RegistrationServiceImpl;
import com.querydsl.core.types.Predicate;
import com.ntnu.gidd.utils.JpaUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    @Mock
    private UserRepository userRepository;

    @Mock
    private ActivityRepository activityRepository;

    private Registration registration;
    private Registration registration2;
    private List<Registration> registrationsExpected;
    private Predicate predicate;
    private Pageable pageable;

    @BeforeEach
    public void setUp() throws Exception {

        registration = new RegistrationFactory().getObject();
        registration2 = new RegistrationFactory().getObject();

        assert registration != null;
        assert registration2 != null;

        when(userRepository.save(registration.getUser())).thenReturn(registration.getUser());
        when(userRepository.save(registration2.getUser())).thenReturn(registration2.getUser());
        userRepository.save(registration.getUser());
        userRepository.save(registration2.getUser());

        when(activityRepository.save(registration.getActivity())).thenReturn(registration.getActivity());
        when(activityRepository.save(registration2.getActivity())).thenReturn(registration2.getActivity());
        activityRepository.save(registration.getActivity());
        activityRepository.save(registration2.getActivity());

        when(userRepository.findById(registration.getUser().getId())).thenReturn(Optional.ofNullable(registration.getUser()));
        when(userRepository.findById(registration2.getUser().getId())).thenReturn(Optional.ofNullable(registration2.getUser()));
        when(activityRepository.findById(registration.getActivity().getId())).thenReturn(Optional.ofNullable(registration.getActivity()));
        when(activityRepository.findById(registration2.getActivity().getId())).thenReturn(Optional.ofNullable(registration2.getActivity()));

        when(registrationRepository.save(registration)).thenReturn(registration);
        when(registrationRepository.save(registration2)).thenReturn(registration2);
        registrationService.saveRegistration(registration.getUser().getId(), registration.getActivity().getId());
        registrationService.saveRegistration(registration2.getUser().getId(), registration2.getActivity().getId());

        predicate = JpaUtils.getEmptyPredicate();
        pageable = JpaUtils.getDefaultPageable();
    }

    @AfterEach
    public void cleanUp(){
        registrationRepository.delete(registration);
        registrationRepository.delete(registration2);
    }

    @Test
    void testRegistrationServiceImplGetRegistrationForActivityReturnsRegistration(){
        registrationsExpected = List.of(registration);

        when(registrationRepository.findAll(any(Predicate.class), any(PageRequest.class))).thenReturn(new PageImpl<>(registrationsExpected, pageable, registrationsExpected.size()));
        List<RegistrationUserDto> registrationsFound = registrationService.getRegistrationForActivity(predicate, pageable,
                                                                                                  registration.getActivity().getId())
                .getContent();

        for (int i = 0; i < registrationsFound.size(); i++) {
            assertThat(registrationsFound.get(i).getUser().getEmail()).isEqualTo(registrationsExpected.get(i).getUser().getEmail());
            assertThat(registrationsFound.get(i)).isNotNull();
        }
    }

    @Test
    void testRegistrationServiceImplGetRegistrationForUserReturnsRegistration(){
        registrationsExpected = List.of(registration, registration2);
        when(registrationRepository.findAll(any(Predicate.class), any(PageRequest.class))).thenReturn(new PageImpl<>(registrationsExpected, pageable, registrationsExpected.size()));
        when(userRepository.findByEmail(registration.getUser().getEmail())).thenReturn(Optional.of(registration.getUser()));
        List<RegistrationActivityListDto> registrationsFound = registrationService.getRegistrationWithUsername(predicate, pageable,
                                                                                                   registration.getUser().getEmail())
                .getContent();

        for (int i = 0; i < registrationsExpected.size(); i++) {
            assertThat(registrationsFound.get(i).getActivity().getTitle()).isEqualTo(registrationsExpected.get(i).getActivity().getTitle());
            assertThat(registrationsFound.get(i)).isNotNull();
        }
    }

    @Test
    void testRegistrationServiceImplGetRegistrationWithUsernameReturnsRegistration(){
        registrationsExpected = List.of(registration, registration2);
        when(registrationRepository.findAll(any(Predicate.class), any(PageRequest.class))).thenReturn(new PageImpl<>(registrationsExpected, pageable, registrationsExpected.size()));
        when(userRepository.findByEmail(registration.getUser().getEmail())).thenReturn(Optional.ofNullable(registration.getUser()));
        List<RegistrationActivityListDto> registrationsFound = registrationService.getRegistrationWithUsername(predicate, pageable,
                                                                                                   registration.getUser().getEmail()).getContent();

        for (int i = 0; i < registrationsExpected.size(); i++) {
            assertThat(registrationsFound.get(i).getActivity().getTitle()).isEqualTo(registrationsExpected.get(i).getActivity().getTitle());
            assertThat(registrationsFound.get(i)).isNotNull();
        }
    }

    @Test
    void testGetRegistrationWithUsernameAndActivityId(){
        lenient().when(registrationRepository
                .findRegistrationByUser_IdAndActivity_Id(registration.getUser().getId(), registration.getActivity().getId()))
                .thenReturn(Optional.ofNullable(registration));
        when(userRepository.findByEmail(registration.getUser().getEmail())).thenReturn(Optional.ofNullable(registration.getUser()));

        RegistrationActivityDto registrationFound = registrationService.getRegistrationWithUsernameAndActivityId(registration.getUser().getEmail(), registration.getActivity().getId());

        assertThat(registrationFound.getActivity().getTitle()).isEqualTo(registration.getActivity().getTitle());
        assertThat(registrationFound).isNotNull();

    }

    @Test
    void testRegistrationServiceImplGetRegistrationWithRegistrationId(){
        when(registrationRepository.findById(registration.getRegistrationId())).thenReturn(Optional.ofNullable(registration));
        RegistrationUserDto registrationFound = registrationService.getRegistrationWithRegistrationId(registration.getRegistrationId());

        assertThat(registrationFound.getUser().getEmail()).isEqualTo(registration.getUser().getEmail());
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
            assertThat(registrationsFound.get(i).getUser().getEmail()).isEqualTo(registrationsExpected.get(i).getUser().getEmail());
        }
    }

    @Test
    void testRegistrationServiceImplDeleteRegistrationWithUsernameAndActivityId(){
        registrationsExpected = List.of(registration2);

        lenient().when(registrationRepository.findRegistrationByUser_IdAndActivity_Id(registration.getUser().getId(), registration.getActivity().getId())).thenReturn(Optional.ofNullable(registration));
        when(userRepository.findByEmail(registration.getUser().getEmail())).thenReturn(Optional.ofNullable(registration.getUser()));
        doNothing().when(registrationRepository).deleteRegistrationsByUser_IdAndActivity_Id(registration.getUser().getId(), registration.getActivity().getId());

        registrationService.deleteRegistrationWithUsernameAndActivityId(registration.getUser().getEmail(), registration.getActivity().getId());

        when(registrationRepository.findAll()).thenReturn(registrationsExpected);
        List<Registration> registrationsFound = registrationRepository.findAll();

        for (int i = 0; i < registrationsExpected.size(); i++){
            assertThat(registrationsFound.get(i).getUser().getEmail()).isEqualTo(registrationsExpected.get(i).getUser().getEmail());
        }
    }


}
