package com.ntnu.gidd.service;

import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.UserRepository;
import com.ntnu.gidd.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
@NoArgsConstructor
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
}