package com.ntnu.gidd.config;

import java.util.Arrays;
import java.util.Collections;

import com.ntnu.gidd.model.User;
import com.ntnu.gidd.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
            return;

        User account = new User();
        account.setEmail("admin@test.com");
        account.setPassword(encoder.encode("admin"));

        log.info("[x] Preloading {}", userRepository.save(account));

        alreadySetup = true;
    }
}