package com.ntnu.gidd.factories;

import com.ntnu.gidd.model.User;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.ntnu.gidd.utils.StringRandomizer.getRandomEmail;
import static com.ntnu.gidd.utils.StringRandomizer.getRandomString;

public class UserFactory implements FactoryBean<User> {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public User getObject() {
        return User.builder()
                .id(UUID.randomUUID())
                .email(getRandomEmail())
                .firstName(getRandomString(5))
                .surname(getRandomString(5))
                .password(encoder.encode(getRandomString(10)))
                .salt(getRandomString(32))
                .birthDate(LocalDate.now())
                .created_at(LocalDateTime.now())
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}