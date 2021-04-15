package com.ntnu.gidd.service;

import com.ntnu.gidd.dto.UserUpdateDto;
import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Slf4j
@NoArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email not found (email:" + email +")"));

        log.debug("Found user by email, loading user with email: {}", email);

        return UserDetailsImpl.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
    public User updateUser(UUID id, UserUpdateDto user){
        //TODO change exception to UserNotFound when that comes and trainig level
        User updatedUser = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setSurname(user.getSurname());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setBirthDate(user.getBirthDate());
        return userRepository.save(updatedUser);

    }
}
