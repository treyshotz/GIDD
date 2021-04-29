package com.ntnu.gidd.service;

import com.ntnu.gidd.config.ModelMapperConfig;
import com.ntnu.gidd.dto.User.UserDto;
import com.ntnu.gidd.dto.User.UserEmailDto;
import com.ntnu.gidd.dto.User.UserListDto;
import com.ntnu.gidd.dto.User.UserRegistrationDto;
import com.ntnu.gidd.factories.UserFactory;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.service.Comment.CommentService;
import com.ntnu.gidd.service.Registration.RegistrationService;
import com.ntnu.gidd.service.User.UserServiceImpl;
import com.ntnu.gidd.utils.JpaUtils;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ModelMapperConfig.class)
@SpringBootTest
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RegistrationService registrationService;

    @Mock
    private CommentService commentService;

    @Mock
    BCryptPasswordEncoder encoder;

    @Autowired
    ModelMapper modelMapper = new ModelMapper();

    private User user;
    private User secondUser;
    private List<User> usersExpected;
    private UserRegistrationDto userRegistrationDto;
    private Predicate predicate;
    private Pageable pageable;

    @BeforeEach
    void setUp() throws Exception {
        user = new UserFactory().getObject();
        userRegistrationDto = modelMapper.map(user, UserRegistrationDto.class);
        userRepository.save(user);

        predicate = JpaUtils.getEmptyPredicate();
        pageable = JpaUtils.getDefaultPageable();
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    void testGetUserDtoByEmailReturnsUserDto() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserDto userDtoFound = userService.getUserDtoByEmail(user.getEmail());
        assertThat(userDtoFound.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void testDeleteUserDeletesUser() throws Exception{
        secondUser = new UserFactory().getObject();
        userRepository.save(secondUser);
        usersExpected = List.of(user, secondUser);
        when(userRepository.findAll(any(Predicate.class), any(PageRequest.class))).thenReturn(new PageImpl<>(usersExpected, pageable, usersExpected.size()));
        Page<UserDto> usersFound = userService.getAll(predicate, pageable);

        assertThat(usersFound.stream().findFirst().get().getEmail()).
                isEqualTo(usersExpected.get(0).getEmail());
        assertThat(usersFound.getTotalElements())
                .isEqualTo(Long.valueOf(usersExpected.size()));

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(user.getId());
        doNothing().when(registrationService).deleteAllRegistrationsWithUsername(user.getEmail());
        doNothing().when(commentService).deleteAllCommentsOnUser(user.getEmail());
        userService.deleteUser(user.getEmail());

        List<User> usersExpectedAfterDelete = List.of(secondUser);
        when(userRepository.findAll(any(Predicate.class), any(PageRequest.class))).thenReturn(new PageImpl<>(usersExpectedAfterDelete, pageable, usersExpected.size() - 1));
        Page<User> usersFound2 = userRepository.findAll(predicate, pageable);

        assertThat(usersFound2.getTotalElements())
                .isEqualTo(Long.valueOf(usersExpected.size() - 1));
        assertThat(usersFound2.stream().findFirst().get().getEmail()).
                isEqualTo(usersExpected.get(1).getEmail());
    }
}
